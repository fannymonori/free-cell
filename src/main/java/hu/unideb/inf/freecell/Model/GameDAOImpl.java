/*
 * Copyright 2016 University of Debrecen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.unideb.inf.freecell.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author fanny
 */
public class GameDAOImpl implements GameDAO {

    String pathHome;

    static Logger LOGGER = LoggerFactory.getLogger(GameDAOImpl.class);

    private final DocumentBuilderFactory dbFactory;

    public GameDAOImpl() {
        pathHome = System.getProperty("user.home") + "/.freecell/";
        dbFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    public List<String> getSavedGames() {
        List<String> savedGames = new ArrayList<>();
        try {
            File freecellDir = new File(pathHome);
            File[] listing = freecellDir.listFiles();

            if (listing != null) {
                for (File file : listing) {
                    LOGGER.info(file.getName());
                    LOGGER.info(file.getPath());
                    if (file.isFile()) {

                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(file.getPath());

                        doc.getDocumentElement().normalize();

                        NodeList nlist = doc.getElementsByTagName("game");

                        for (int i = 0; i < nlist.getLength(); i++) {
                            Node n = nlist.item(i);
                            Element element = (Element) n;
                            savedGames.add(element.getAttribute("name"));

                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.info(ex.getMessage());
        }

        return savedGames;
    }

    @Override
    public void saveGame(Game game, String name) {
        LOGGER.info("Saving game...");

        File destDir = new File(pathHome);
        if (!destDir.exists()) {
            LOGGER.info("Creating directory");
            destDir.mkdir();
        }

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("freecell");
            doc.appendChild(rootElement);

            Element savedGameElement = doc.createElement("game");
            rootElement.appendChild(savedGameElement);
            Attr nameAttr = doc.createAttribute("name");
            nameAttr.setValue(name);
            savedGameElement.setAttributeNode(nameAttr);

            Element tableauElement = doc.createElement("tableau");
            savedGameElement.appendChild(tableauElement);

            //Saving tableau elements
            for (int i = 0; i < 8; i++) {
                Element pileElement = doc.createElement("pile");
                tableauElement.appendChild(pileElement);

                for (Card c : game.tableau.get(i)) {
                    if (c.getRank() != 0) {
                        Element cardElement = doc.createElement("card");
                        pileElement.appendChild(cardElement);
                        Attr suitAttr = doc.createAttribute("suit");
                        suitAttr.setValue(c.getSuit());
                        Attr rankAttr = doc.createAttribute("rank");
                        rankAttr.setValue(Integer.toString(c.getRank()));
                        cardElement.setAttributeNode(suitAttr);
                        cardElement.setAttributeNode(rankAttr);
                    }
                }
            }

            //saving homecells
            Element homecellsElement = doc.createElement("homecells");
            savedGameElement.appendChild(homecellsElement);

            for (Map.Entry<String, List<Card>> entry : game.homeCells.piles.entrySet()) {
                Element pileElement = doc.createElement("pile");
                homecellsElement.appendChild(pileElement);
                Attr suitAttrH = doc.createAttribute("suit");
                suitAttrH.setValue(entry.getKey());
                pileElement.setAttributeNode(suitAttrH);

                for (Card c : entry.getValue()) {
                    if (c.getRank() != 0) {
                        Element cardElement = doc.createElement("card");
                        pileElement.appendChild(cardElement);
                        Attr suitAttr = doc.createAttribute("suit");
                        suitAttr.setValue(c.getSuit());
                        Attr rankAttr = doc.createAttribute("rank");
                        rankAttr.setValue(Integer.toString(c.getRank()));
                        cardElement.setAttributeNode(suitAttr);
                        cardElement.setAttributeNode(rankAttr);
                    }
                }
            }

            //saving freecells
            Element freeCellsElement = doc.createElement("freecells");
            savedGameElement.appendChild(freeCellsElement);
            for (Card c : game.freeCells.cards) {
                Element cardElement = doc.createElement("card");
                freeCellsElement.appendChild(cardElement);
                Attr suitAttr = doc.createAttribute("suit");
                suitAttr.setValue(c.getSuit());
                Attr rankAttr = doc.createAttribute("rank");
                rankAttr.setValue(Integer.toString(c.getRank()));
                cardElement.setAttributeNode(suitAttr);
                cardElement.setAttributeNode(rankAttr);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(pathHome + name + ".xml"));
            transformer.transform(source, result);

            LOGGER.info("Game saved: " + name + "!");

        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(GameDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            java.util.logging.Logger.getLogger(GameDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            java.util.logging.Logger.getLogger(GameDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Saving game is done!");
    }

    @Override
    public void loadGame(Game game, String name) {
        LOGGER.info("Loading game: " + name + "...");
        
        try {

            File xmlFile = new File(pathHome + name + ".xml");

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            game.tableau = new ArrayList<>();
            game.homeCells = new HomeCells();
            game.freeCells = new FreeCells();

            NodeList nlist = doc.getElementsByTagName("game");
            for (int i = 0; i < nlist.getLength(); i++) {

                Node gameElement = nlist.item(i);
                Element element = (Element) gameElement;

                String s = element.getAttributeNode("name").getValue();

                if (s.equals(name)) {
                    NodeList childNodes = element.getChildNodes();
                    for (int index = 0; index < childNodes.getLength(); index++) {
                        Node n = childNodes.item(index);
                        //LOGGER.info(n.getNodeName());
                        if (n.getNodeName().equals("tableau")) {
                            Element pilesElement = (Element) n;
                            NodeList piles = pilesElement.getElementsByTagName("pile");
                            for (int p = 0; p < piles.getLength(); p++) {
                                List<Card> pile = new ArrayList<>();
                                for (int c = 0; c < piles.item(p).getChildNodes().getLength(); c++) {
                                    Element card = (Element) piles.item(p).getChildNodes().item(c);
                                    if (card.hasAttribute("suit") && card.hasAttribute("rank")) {
                                        Card newCard = new Card(card.getAttribute("suit"),
                                                Integer.parseInt(card.getAttribute("rank")));
                                        pile.add(newCard);
                                    }
                                }
                                game.tableau.add(pile);
                            }
                        } else if (n.getNodeName().equals("homecells")) {
                            Element pilesElement = (Element) n;
                            NodeList piles = pilesElement.getElementsByTagName("pile");

                            for (int p = 0; p < piles.getLength(); p++) {
                                List<Card> list = new ArrayList<>();

                                Element pile = (Element) piles.item(p);

                                for (int c = 0; c < pile.getChildNodes().getLength(); c++) {
                                    Element card = (Element) pile.getChildNodes().item(c);
                                    if (card.hasAttribute("suit") && card.hasAttribute("rank")) {
                                        Card newCard = new Card(card.getAttribute("suit"),
                                                Integer.parseInt(card.getAttribute("rank")));

                                        list.add(newCard);
                                    }
                                }

                                if (pile.getChildNodes().getLength() == 0) {
                                    Card newCard = new Card(pile.getAttribute("suit"), 0);
                                    list.add(newCard);
                                }

                                game.homeCells.piles.put(pile.getAttribute("suit"), list);
                            }
                        } else if (n.getNodeName().equals("freecells")) {
                            Element cardsElement = (Element) n;
                            NodeList cards = cardsElement.getElementsByTagName("card");
                            for (int c = 0; c < cards.getLength(); c++) {
                                Element card = (Element) cards.item(c);
                                Card newCard = new Card(card.getAttribute("suit"),
                                        Integer.parseInt(card.getAttribute("rank")));
                                game.freeCells.cards.add(newCard);

                            }

                        }

                    }

                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            java.util.logging.Logger.getLogger(GameDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Game loading is finished!");
    }

}
