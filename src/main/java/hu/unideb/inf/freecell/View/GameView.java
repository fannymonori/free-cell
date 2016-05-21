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
package hu.unideb.inf.freecell.View;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import org.slf4j.*;

/**
 *
 * @author fanny
 */
public class GameView extends Pane {

    //public List<PileView> tableau;
    public List<List<CardView>> tableau;

    public List<HomeCellView> HomeCells;

    public List<FreeCellView> FreeCells;

    static Logger LOGGER = LoggerFactory.getLogger(GameView.class);

    public GameView() {
        tableau = new ArrayList<>();
        HomeCells = new ArrayList<>();
        FreeCells = new ArrayList<>();
    }

    /*public void removeFromTableauByID(CardView cardview) {
        for (List<CardView> cvl : tableau) {
            for (CardView cv : cvl) {
                if (cardview.getId() == cv.getId()) {
                    cvl.remove(cv);
                }
            }
        }
    }*/

    /*public void addToTableau(CardView cardViewSource, CardView cardViewTarget) {
        for (List<CardView> cvl : tableau) {
            for (CardView cv : cvl) {
                if(cv.getId() == cardViewTarget.getId()){
                    cvl.add(cardViewSource);
                }
            }
        }
    }*/

}
