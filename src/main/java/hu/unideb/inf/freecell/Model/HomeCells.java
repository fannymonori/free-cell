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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fanny
 */
public class HomeCells{
    //Map<String,Pile> piles;
    public Map<String, List<Card>> piles;

    public HomeCells() {
        piles = new HashMap<>();
        
        for(String s: Arrays.asList("clubs", "diamonds", "hearts", "spades")){
            List<Card> p = new ArrayList<>();
            p.add(new Card(s,0));
            piles.put(s, p);
        }
    }

}
