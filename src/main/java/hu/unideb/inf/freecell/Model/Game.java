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

import java.util.List;

/**
 * Interface that represents the game called FreeCell, and its rules.
 * @author fanny
 */
public interface Game {

    /**
     * Places one card on another in the tableau deck;
     * @param cardSource The card to be placed on the tableau.
     * @param cardTarget The card to be placed on.
     * @return True if the move was successful. Return false if the rules of the game does not permit this action.
     */
    public boolean addToTableau(Card cardSource, Card cardTarget);

    /**
     * Places one card on one of the home cells;
     * @param cardSource The card to be placed on one of the home cells.
     * @param cardTarget The card to be placed on. It must be one of the home cells.
     * @return True if the move was successful. Return false if the rules of the game does not permit this action.
     */
    public boolean addToHomeCell(Card cardSource, Card cardTarget);

    /**
     * Places one card in the free cell pile;
     * @param cardSource The card to be placed in the free cell pile.
     * @return True if the move was successful. Return false if the rules of the game does not permit this action.
     */
    public boolean addToFreeCell(Card cardSource);

    /**
     * Decides whether a card is at the last place among the tableau piles.
     * @param card
     * @return True if the card is at the last place.
     */
    public boolean isLast(Card card);

    /**
     * Decides whether the game has been won.
     * @return True is the game is finished.
     */
    public boolean hasWon();

    /**
     *
     * @return
     */
    public FreeCells getFreeCells();
    
    /**
     * 
     * @return 
     */
    public HomeCells getHomeCells();
    
    /**
     * 
     * @return 
     */
    public List<List<Card>> getTableau();
    
    /**
     * 
     * @param f 
     */
    public void setFreeCell(FreeCells f);
    
    /**
     * 
     * @param h 
     */
    public void setHomeCell(HomeCells h);
    
    /**
     * 
     * @param t 
     */
    public void setTableau(List<List<Card>> t);

    /**
     * Initializes a new game.
     */
    public void newGame();

    /**
     * 
     * @param name 
     */
    public void saveGame(String name);

    /**
     * 
     * @param name 
     */
    public void loadGame(String name);

    /**
     * 
     * @return 
     */
    public List<String> getSavedGames();
}
