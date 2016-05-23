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
package hu.unideb.inf.freecell.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Implementation of the Game interface.
 * @author fanny
 */
public class GameImpl implements Game {

    static Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private Tableau tableau;

    private HomeCells homeCells;

    private FreeCells freeCells;

    public GameImpl() {

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

        tableau = new Tableau();

        int it = 0;
        for (int i = 0; i < 8; i++) {

            List<Card> pile = new ArrayList<>();
            for (int j = 0; j < (i < 4 ? 7 : 6); j++) {
                pile.add(deck.get(it));
                it++;
            }
            tableau.piles.add(pile);
        }

        homeCells = new HomeCells();
        freeCells = new FreeCells();
    }

    @Override
    public boolean addToTableau(Card cardSource, Card cardTarget) {

        if (!this.isLast(cardTarget)) {
            return false;
        }
        if (cardSource.getColour() == cardTarget.getColour()) {
            return false;
        }
        if (cardSource.getRank() >= cardTarget.getRank()) {
            return false;
        }
        if (Math.abs(cardSource.getRank() - cardTarget.getRank()) > 1) {
            return false;
        }

        if (isTableauCard(cardSource)) {
            if (!isLast(cardSource)) {
                return false;
            } else {
                int targetIndex = getCardPileIndex(cardTarget);
                int sourceIndex = getCardPileIndex(cardSource);

                List<Card> p = new ArrayList<>();
                for (int i = 0; i < tableau.piles.get(targetIndex).size(); i++) {
                    p.add(new Card(tableau.piles.get(targetIndex).get(i).getSuit(), tableau.piles.get(targetIndex).get(i).getRank()));
                }
                p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
                tableau.piles.set(targetIndex, p);

                List<Card> p2 = new ArrayList<>();
                for (int i = 0; i < tableau.piles.get(sourceIndex).size() - 1; i++) {
                    p2.add(new Card(tableau.piles.get(sourceIndex).get(i).getSuit(), tableau.piles.get(sourceIndex).get(i).getRank()));
                }

                if (tableau.piles.get(sourceIndex).size() - 1 == 0) {
                    p2.add(new Card("", 0));
                }

                tableau.piles.set(sourceIndex, p2);
            }
        } else if (isFreeCellCard(cardSource)) {
            int index = -1;
            for (int i = 0; i < freeCells.cards.size(); i++) {
                if (freeCells.cards.get(i).equals(cardSource)) {
                    index = i;
                }
            }

            int targetIndex = getCardPileIndex(cardTarget);

            List<Card> p = new ArrayList<>();
            for (int i = 0; i < tableau.piles.get(targetIndex).size(); i++) {
                p.add(new Card(tableau.piles.get(targetIndex).get(i).getSuit(), tableau.piles.get(targetIndex).get(i).getRank()));
            }
            p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
            tableau.piles.set(targetIndex, p);

            freeCells.cards.remove(index);
        }

        return true;
    }

    @Override
    public boolean addToHomeCell(Card cardSource, Card cardTarget) {

        if (!isHomeCellCard(cardTarget)) {
            return false;
        }
        if (!cardSource.getSuit().equals(cardTarget.getSuit())) {
            return false;
        }
        if (cardSource.getRank() <= cardTarget.getRank()) {
            return false;
        }
        if (Math.abs(cardSource.getRank() - cardTarget.getRank()) > 1) {
            return false;
        }

        int sourceIndex = getCardPileIndex(cardSource);

        List<Card> p = new ArrayList<>();
        for (int i = 0; i < homeCells.piles.get(cardTarget.getSuit()).size(); i++) {
            p.add(new Card(homeCells.piles.get(cardTarget.getSuit()).get(i).getSuit(), homeCells.piles.get(cardTarget.getSuit()).get(i).getRank()));
        }
        p.add(new Card(cardSource.getSuit(), cardSource.getRank()));
        homeCells.piles.put(cardTarget.getSuit(), p);

        if (isTableauCard(cardSource)) {
            List<Card> p2 = new ArrayList<>();
            for (int i = 0; i < tableau.piles.get(sourceIndex).size() - 1; i++) {
                p2.add(new Card(tableau.piles.get(sourceIndex).get(i).getSuit(), tableau.piles.get(sourceIndex).get(i).getRank()));
            }

            if (tableau.piles.get(sourceIndex).size() - 1 == 0) {
                p2.add(new Card("", 0));
            }

            tableau.piles.set(sourceIndex, p2);

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

    @Override
    public boolean addToFreeCell(Card cardSource) {

        if (freeCells.cards.size() == 4) {
            return false;
        }

        freeCells.cards.add(new Card(cardSource.getSuit(), cardSource.getRank()));
        if (isTableauCard(cardSource)) {

            int sourceIndex = getCardPileIndex(cardSource);

            List<Card> p = new ArrayList<>();
            for (int i = 0; i < tableau.piles.get(sourceIndex).size() - 1; i++) {
                p.add(new Card(tableau.piles.get(sourceIndex).get(i).getSuit(), tableau.piles.get(sourceIndex).get(i).getRank()));
            }

            if (tableau.piles.get(sourceIndex).size() - 1 == 0) {
                p.add(new Card("", 0));
            }

            tableau.piles.set(sourceIndex, p);
        }

        return true;
    }

    @Override
    public boolean isLast(Card card) {
        for (int i = 0; i < this.tableau.piles.size(); i++) {
            if (this.tableau.piles.get(i).get(tableau.piles.get(i).size() - 1).equals(card)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasWon() {
        if (!this.homeCells.piles.entrySet().stream().noneMatch((entry) -> (entry.getValue().size() < 14))) {
            return false;
        }

        return true;
    }

    @Override
    public void saveGame(String name) {
        GameDAO gd = new GameDAOImpl();
        gd.saveGame(this, name);
    }

    @Override
    public void loadGame(String name) {

        GameDAO gameDAO = new GameDAOImpl();
        gameDAO.loadGame(this, name);
    }

    @Override
    public List<String> getSavedGames() {
        GameDAO dao = new GameDAOImpl();
        List<String> savedGames = new ArrayList<>(dao.getSavedGames());
        return savedGames;
    }

    @Override
    public void setTableau(Tableau t) {
        this.tableau = new Tableau();
        this.tableau.setPiles(t.getPiles());
    }

    @Override
    public void setFreeCell(FreeCells f) {
        this.freeCells = new FreeCells();
        this.freeCells.setPiles(f.getPiles());
    }

    @Override
    public void setHomeCell(HomeCells h) {
        this.homeCells = new HomeCells();
        this.homeCells.setPiles(h.getPiles());
    }

    @Override
    public FreeCells getFreeCells() {
        return this.freeCells;
    }

    @Override
    public HomeCells getHomeCells() {
        return this.homeCells;
    }

    @Override
    public Tableau getTableau() {
        return this.tableau;
    }
    
    private boolean isTableauCard(Card card) {
        for (int i = 0; i < this.tableau.getPiles().size(); i++) {
            for (int j = 0; j < this.tableau.getPiles().get(i).size(); j++) {
                if (this.tableau.getPiles().get(i).get(j).equals(card)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFreeCellCard(Card card) {
        return freeCells.cards.stream().anyMatch((c) -> (c.equals(card)));
    }

    private boolean isHomeCellCard(Card c) {
        return homeCells.piles.entrySet().stream().anyMatch((entry) -> (entry.getValue().get(entry.getValue().size() - 1).equals(c)));
    }

    private int getCardPileIndex(Card card) {
        for (int i = 0; i < this.tableau.getPiles().size(); i++) {
            if (this.tableau.getPiles().get(i).get(tableau.getPiles().get(i).size() - 1).equals(card)) {
                return i;
            }
        }

        return -1;
    }

}
