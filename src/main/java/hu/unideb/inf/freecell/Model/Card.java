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

/**
 *
 * @author fanny
 */
public class Card {
    
    public enum Colour{BLACK, RED};
    
    private String suit;
    private Colour colour;
    private int rank;

    public Card() {
    }

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        if(suit.equals("clubs") || suit.equals("spades"))
            this.colour = Colour.BLACK;
        else if(suit.equals("diamonds") || suit.equals("hearts"))
            this.colour = Colour.RED;
    }

    public String getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public Colour getColour() {
        return colour;
    }
    

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setValue(int rank) {
        this.rank = rank;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
    

    @Override
    public String toString() {
        return getSuit()+" "+getRank();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        Card c = (Card) obj;
        if(!c.getSuit().equals(this.getSuit()))
            return false;
        if(c.getRank() != this.getRank())
            return false;
        return true;
    }

}
