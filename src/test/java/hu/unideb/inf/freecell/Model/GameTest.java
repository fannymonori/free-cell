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

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fanny
 */
public class GameTest {
    
    private Game gameInstance;
    private Game gameInstance_case2;
    private Game gameInstance_case3;
    
    private List<List<Card>> tableau_case2;
    private List<List<Card>> tableau_case3;
    
    private FreeCells frecell_case2;
    private FreeCells frecell_case3;
    
    private HomeCells homecell_case2;
    private HomeCells homecell_case3;
    
    public GameTest() {
    }
    
    @Before
    public void setUp() {
        gameInstance = new Game();
        
        gameInstance_case2 = new Game();
        
        gameInstance_case3 = new Game();
        
        //test case 2
        tableau_case2 = new ArrayList<>();
        List<Card> pile = new ArrayList<>();
        pile.add(new Card("hearts",10));
        pile.add(new Card("clubs",8));
        tableau_case2.add(pile);
        
        List<Card> pile2 = new ArrayList<>();
        pile2.add(new Card("spades",12));
        pile2.add(new Card("diamonds",7));
        tableau_case2.add(pile2);
        
        frecell_case2 = new FreeCells();
        homecell_case2 = new HomeCells();
        
        gameInstance_case2.setFreeCell(frecell_case2);
        gameInstance_case3.setHomeCell(homecell_case2);
        gameInstance_case2.setTableau(tableau_case2);
        
        //test case 3
        tableau_case3 = new ArrayList<>();
        List<Card> pile_case3_1 = new ArrayList<>();
        pile_case3_1.add(new Card("clubs",8));
        pile_case3_1.add(new Card("hearts",1));
        tableau_case3.add(pile_case3_1);
        
        List<Card> pile_case3_2 = new ArrayList<>();
        pile_case3_2.add(new Card("spades",12));
        pile_case3_2.add(new Card("hearts",2));
        tableau_case3.add(pile_case3_2);
        
        frecell_case3 = new FreeCells();
        homecell_case3 = new HomeCells();
        
        gameInstance_case3.setFreeCell(frecell_case3);
        gameInstance_case3.setHomeCell(homecell_case3);
        gameInstance_case3.setTableau(tableau_case3);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of newGame method, of class Game.
     */
    @Test
    public void testNewGame() {
        System.out.println("newGame");
        
        gameInstance.newGame();
        assertEquals(8, gameInstance.tableau.size());
    }

    /**
     * Test of addToTableau method, of class Game.
     */
    @Test
    public void testAddToTableau() {
        System.out.println("addToTableau");
        Card cardSource = new Card("diamonds",7);
        Card cardTarget = new Card("clubs",8);
        
        boolean success = gameInstance_case2.addToTableau(cardSource, cardTarget);
        assertEquals(true, success);
        assertEquals("diamonds", gameInstance_case2.tableau.get(0).get(gameInstance_case2.tableau.get(0).size()-1).getSuit());
        assertEquals("spades", gameInstance_case2.tableau.get(1).get(gameInstance_case2.tableau.get(1).size()-1).getSuit());
        assertEquals(7, gameInstance_case2.tableau.get(0).get(gameInstance_case2.tableau.get(0).size()-1).getRank());
        assertEquals(12, gameInstance_case2.tableau.get(1).get(gameInstance_case2.tableau.get(1).size()-1).getRank());
    }

    /**
     * Test of addToHomeCell method, of class Game.
     */
    @Test
    public void testAddToHomeCell() {
        System.out.println("addToHomeCell");
        
        //heart 1(ace) from tableau to homecell
        boolean result = gameInstance_case3.addToHomeCell(gameInstance_case3.tableau.get(0).get(gameInstance_case3.tableau.get(0).size()-1),
                gameInstance_case3.homeCells.piles.get("hearts").get(gameInstance_case3.homeCells.piles.get("hearts").size()-1));
        assertEquals(true, result);
        
        assertEquals("clubs", gameInstance_case3.tableau.get(0).get(gameInstance_case3.tableau.get(0).size()-1).getSuit());
        assertEquals("hearts", gameInstance_case3.homeCells.piles.get("hearts").get(gameInstance_case3.homeCells.piles.get("hearts").size()-1).getSuit());
        assertEquals(8, gameInstance_case3.tableau.get(0).get(gameInstance_case3.tableau.get(0).size()-1).getRank());
        assertEquals(1, gameInstance_case3.homeCells.piles.get("hearts").get(gameInstance_case3.homeCells.piles.get("hearts").size()-1).getRank());
    }

    /**
     * Test of addToFreeCell method, of class Game.
     */
//    @Test
//    public void testAddToFreeCell() {
//        System.out.println("addToFreeCell");
//        Card cardSource = null;
//        Game instance = new Game();
//        boolean expResult = false;
//        boolean result = instance.addToFreeCell(cardSource);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of isLast method, of class Game.
     */
//    @Test
//    public void testIsLast() {
//        System.out.println("isLast");
//        Card card = null;
//        Game instance = new Game();
//        boolean expResult = false;
//        boolean result = instance.isLast(card);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of saveGame method, of class Game.
     */
//    @Test
//    public void testSaveGame() {
//        System.out.println("saveGame");
//        String name = "";
//        Game instance = new Game();
//        instance.saveGame(name);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of loadGame method, of class Game.
     */
//    @Test
//    public void testLoadGame() {
//        System.out.println("loadGame");
//        String name = "";
//        Game instance = new Game();
//        instance.loadGame(name);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getSavedGames method, of class Game.
     */
//    @Test
//    public void testGetSavedGames() {
//        System.out.println("getSavedGames");
//        Game instance = new Game();
//        List<String> expResult = null;
//        List<String> result = instance.getSavedGames();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
