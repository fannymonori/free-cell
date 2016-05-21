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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.*;
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
public class Game implements GameI {

    //public Tableau tableau;
    //public List<Pile> tableau;
    public List<List<Card>> tableau;

    public HomeCells homeCells;

    public FreeCells freeCells;

    static Logger LOGGER = LoggerFactory.getLogger(Game.class);

    public Game() {

    }

    @Override
    public void newGame() {
        List<Card> deck = new ArrayList<>();
        Arrays.asList("clubs", "diamonds", "hearts", "spades").stream().forEach((s) -> {
            for (int i = 1; i <= 13; i++) {
                deck.add(new Card(s, i));
            }
        });

        Collections.shuffle(deck, new Random(System.nanoTime()));

        tableau = new ArrayList<>();

        int it = 0;
        for (int i = 0; i < 8; i++) {

            List<Card> pile = new ArrayList<>();
            for (int j = 0; j < (i < 4 ? 7 : 6); j++) {
                pile.add(deck.get(it));
                it++;
            }
            tableau.add(pile);
        }

        homeCells = new HomeCells();
        freeCells = new FreeCells();
    }

    /**
     *
     * @param cardSource
     * @param cardTarget
     * @return
     */
    //@Override
    public boolean addToTableau(Card cardSource, Card cardTarget) {

        if (!this.isLast(cardTarget)) {
            LOGGER.info("1");
            return false;
        }
        if (cardSource.getColour() == cardTarget.getColour()) {
            LOGGER.info("3");
            return false;
        }
        if (cardSource.getRank() >= cardTarget.getRank()) {
            LOGGER.info("4");
            return false;
        }
        if (Math.abs(cardSource.getRank() - cardTarget.getRank()) > 1) {
            LOGGER.info("5");
            return false;
        }

        if (isTableauCard(cardSource)) {
            if (!isLast(cardSource)) {
                LOGGER.info("2");
                return false;
            } else {

                //REFERENCUÁKRA MINDENHOL FIGYELNI
                int targetIndex = getCardPileIndex(cardTarget);
                int sourceIndex = getCardPileIndex(cardSource);

                List<Card> p = new ArrayList<>();
                for (int i = 0; i < tableau.get(targetIndex).size(); i++) {
                    p.add(new Card(tableau.get(targetIndex).get(i).getSuit(), tableau.get(targetIndex).get(i).getRank()));
                }
                p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
                tableau.set(targetIndex, p);

                List<Card> p2 = new ArrayList<>();
                for (int i = 0; i < tableau.get(sourceIndex).size() - 1; i++) {
                    p2.add(new Card(tableau.get(sourceIndex).get(i).getSuit(), tableau.get(sourceIndex).get(i).getRank()));
                }

                if (tableau.get(sourceIndex).size() - 1 == 0) {
                    p2.add(new Card("", 0));
                }

                tableau.set(sourceIndex, p2);
            }
        } else if (isFreeCellCard(cardSource)) {
            int index = -1;
            for (int i = 0; i < freeCells.cards.size(); i++) {
                if (freeCells.cards.get(i).equals(cardSource)) {
                    index = i;
                }
            }

            int targetIndex = getCardPileIndex(cardTarget);
            int sourceIndex = getCardPileIndex(cardSource);

            List<Card> p = new ArrayList<>();
            for (int i = 0; i < tableau.get(targetIndex).size(); i++) {
                p.add(new Card(tableau.get(targetIndex).get(i).getSuit(), tableau.get(targetIndex).get(i).getRank()));
            }
            p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
            tableau.set(targetIndex, p);

            //EBBŐL MÉG BAJ LHET
            freeCells.cards.remove(index);
        }

        return true;
    }

    public boolean addToHomeCell(Card cardSource, Card cardTarget) {

        if (!isHomeCellCard(cardTarget)) {
            LOGGER.info("1");
            return false;
        }
        if (!cardSource.getSuit().equals(cardTarget.getSuit())) {
            LOGGER.info("2");
            return false;
        }
        if (cardSource.getRank() <= cardTarget.getRank()) {
            LOGGER.info("3");
            return false;
        }
        if (Math.abs(cardSource.getRank() - cardTarget.getRank()) > 1) {
            LOGGER.info("4");
            return false;
        }

        int targetIndex = getCardPileIndex(cardTarget);
        int sourceIndex = getCardPileIndex(cardSource);

        List<Card> p = new ArrayList<>();
        for (int i = 0; i < homeCells.piles.get(cardTarget.getSuit()).size(); i++) {
            p.add(new Card(homeCells.piles.get(cardTarget.getSuit()).get(i).getSuit(), homeCells.piles.get(cardTarget.getSuit()).get(i).getRank()));
        }
        p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
        homeCells.piles.put(cardTarget.getSuit(), p);

        //=================================777
        if (isTableauCard(cardSource)) {
            List<Card> p2 = new ArrayList<>();
            for (int i = 0; i < tableau.get(sourceIndex).size() - 1; i++) {
                p2.add(new Card(tableau.get(sourceIndex).get(i).getSuit(), tableau.get(sourceIndex).get(i).getRank()));
            }

            if (tableau.get(sourceIndex).size() - 1 == 0) {
                p2.add(new Card("", 0));
            }

            tableau.set(sourceIndex, p2);

        } else if (isFreeCellCard(cardTarget)) {
            int index = -1;
            for (int i = 0; i < freeCells.cards.size(); i++) {
                if (freeCells.cards.get(i).equals(cardSource)) {
                    index = i;
                }
            }
            freeCells.cards.remove(index);
        }

        return true;
    }

    public boolean addToFreeCell(Card cardSource) {

        if (freeCells.cards.size() == 4) {
            LOGGER.info("You can't put more card to the FreeCell pile!");
            return false;
        }

        //homecell to freecell not valid move
        freeCells.cards.add(new Card(cardSource.getSuit(), cardSource.getRank()));
        if (isTableauCard(cardSource)) {

            int sourceIndex = getCardPileIndex(cardSource);

            List<Card> p = new ArrayList<>();
            for (int i = 0; i < tableau.get(sourceIndex).size() - 1; i++) {
                p.add(new Card(tableau.get(sourceIndex).get(i).getSuit(), tableau.get(sourceIndex).get(i).getRank()));
            }

            if (tableau.get(sourceIndex).size() - 1 == 0) {
                p.add(new Card("", 0));
            }

            tableau.set(sourceIndex, p);
        }

        return true;
    }

    public boolean isHomeCellCard(Card c) {
        return homeCells.piles.entrySet().stream().anyMatch((entry) -> (entry.getValue().get(entry.getValue().size() - 1).equals(c)));
    }

    private int getCardPileIndex(Card card) {
        for (int i = 0; i < this.tableau.size(); i++) {
            //LOGGER.info(tableau.get(i).peekCard().toString());
            if (this.tableau.get(i).get(tableau.get(i).size() - 1).equals(card)) {
                return i;
            }
        }

        return -1;
    }

    public boolean isLast(Card card) {
        for (int i = 0; i < this.tableau.size(); i++) {
            //LOGGER.info("last:" + this.tableau.piles.get(i).peekCard().getSuit());
            //LOGGER.info(card.getSuit());
            if (this.tableau.get(i).get(tableau.get(i).size() - 1).equals(card)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTableauCard(Card card) {
        for (int i = 0; i < this.tableau.size(); i++) {
            for (int j = 0; j < this.tableau.get(i).size(); j++) {
                if (this.tableau.get(i).get(j).equals(card)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFreeCellCard(Card card) {
        return freeCells.cards.stream().anyMatch((c) -> (c.equals(card)));
    }

    public void saveGame(String name) {
        
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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
            
            for(int i = 0; i < 8; i++){
                Element pileElement = doc.createElement("pile");
                tableauElement.appendChild(pileElement);
                
                for(Card c: tableau.get(i)){
                    if(c.getRank() != 0){
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
            
            Element homecellsElement = doc.createElement("homecells");
            savedGameElement.appendChild(homecellsElement);
             
                for (Map.Entry<String,List<Card>> entry : homeCells.piles.entrySet()) {
                    Element pileElement = doc.createElement("pile");
                     homecellsElement.appendChild(pileElement);
                     Attr suitAttrH = doc.createAttribute("suit");
                     suitAttrH.setValue(entry.getKey());
                     pileElement.setAttributeNode(suitAttrH);
                     
                     for(Card c: entry.getValue()){
                        if(c.getRank() != 0){
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
                
            Element freeCellsElement = doc.createElement("freecells");
            savedGameElement.appendChild(freeCellsElement);
            for(Card c: freeCells.cards){
                 Element cardElement = doc.createElement("card");
                 freeCellsElement.appendChild(cardElement);
                 Attr suitAttr = doc.createAttribute("suit");
                 suitAttr.setValue(c.getSuit());
                 Attr rankAttr = doc.createAttribute("rank");
                 rankAttr.setValue(Integer.toString(c.getRank()));
                 cardElement.setAttributeNode(suitAttr);
                 cardElement.setAttributeNode(rankAttr);
            }
            
            
            //creating the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            String homeDir = System.getProperty("user.home");
            boolean dir = new File(homeDir+"/.freecell").mkdir();
            StreamResult result = new StreamResult(new File(homeDir+"/.freecell/"+"savedgame.xml"));
            transformer.transform(source, result);
            
        } catch (ParserConfigurationException ex) {
            LOGGER.error(ex.getMessage());
        } catch (TransformerConfigurationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void LoadGame(String name){
        
        try {
            String homeDir = System.getProperty("user.home");
            File xmlFile = new File(homeDir+"/.freecell/"+"savedgame.xml");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            doc.getDocumentElement().normalize();
            
             tableau = new ArrayList<>();
             homeCells = new HomeCells();
             freeCells = new FreeCells();
             
             NodeList nlist = doc.getElementsByTagName("game");
             for(int i = 0; i < nlist.getLength(); i++){
                 
                 Node game = nlist.item(i);
                 Element element = (Element) game;
                 
                 String s = element.getAttributeNode("name").getValue();
                 
                 if(s.equals(name)){
                     NodeList childNodes = element.getChildNodes();
                     for(int index = 0; index < childNodes.getLength(); index++){
                         Node n = childNodes.item(index);
                         //LOGGER.info(n.getNodeName());
                         if(n.getNodeName().equals("tableau")){
                             Element pilesElement = (Element) n;
                             NodeList piles = pilesElement.getElementsByTagName("pile");
                             for(int p = 0; p < piles.getLength(); p++){
                                 List<Card> pile = new ArrayList<>();
                                 for(int c = 0; c < piles.item(p).getChildNodes().getLength(); c++){
                                     Element card = (Element) piles.item(p).getChildNodes().item(c);
                                     if(card.hasAttribute("suit") && card.hasAttribute("rank")){
                                         Card newCard = new Card(card.getAttribute("suit"),
                                                                 Integer.parseInt(card.getAttribute("rank")));
                                         pile.add(newCard);
                                     }
                                 }
                                 tableau.add(pile);
                             }
                         }
                         else if(n.getNodeName().equals("homecells")){
                             Element pilesElement = (Element) n;
                             NodeList piles = pilesElement.getElementsByTagName("pile");
                             
                             for(int p = 0; p < piles.getLength(); p++){
                                 List<Card> list = new ArrayList<>();
                                 
                                 Element pile = (Element) piles.item(p);
                                 
                                 for(int c = 0; c < pile.getChildNodes().getLength(); c++){
                                     Element card = (Element) pile.getChildNodes().item(c);
                                     if(card.hasAttribute("suit") && card.hasAttribute("rank")){
                                         Card newCard = new Card(card.getAttribute("suit"),
                                                                 Integer.parseInt(card.getAttribute("rank")));
                                         
                                         list.add(newCard);
                                     }
                                 }
                                 
                                 if(pile.getChildNodes().getLength() == 0){
                                     Card newCard = new Card(pile.getAttribute("suit"),0);
                                     list.add(newCard);
                                 }
                                 
                                 homeCells.piles.put(pile.getAttribute("suit"), list);
                             }
                         }
                         else if(n.getNodeName().equals("freecells")){
                             //List<Card> list = new ArrayList<>();
                             Element cardsElement = (Element) n;
                             NodeList cards = cardsElement.getElementsByTagName("card");
                             for(int c = 0; c < cards.getLength(); c++){
                                 Element card = (Element) cards.item(c);
                                 //LOGGER.info(card.getAttribute("suit"));
                                 Card newCard = new Card(card.getAttribute("suit"),
                                 Integer.parseInt(card.getAttribute("rank")));
                                 freeCells.cards.add(newCard);
                                 
                             }
                             
                             
                         }
                         
                         
                     }
                     
                     
                 }
             }
            
        
             
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public List<String> getSavedGames(){
        List<String> savedGames = new ArrayList<>();
         try {
            String homeDir = System.getProperty("user.home");
            File xmlFile = new File(homeDir+"/.freecell/"+"savedgame.xml");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            doc.getDocumentElement().normalize();
            
            NodeList nlist = doc.getElementsByTagName("game");
            
            for(int i = 0; i < nlist.getLength(); i++){
                Node n = nlist.item(i);
                    Element element = (Element) n;
                    savedGames.add(element.getAttribute("name"));
                
                
            }
            
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return savedGames;
    }
}
