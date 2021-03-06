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
package hu.unideb.inf.freecell.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author fanny
 */
public class CardView extends ImageView {

    ///private String imageURL;
    private Image image;
    //private String id;

    private double mouseDeltaX;
    private double mouseDeltaY;
    private double origX;
    private double origY;

    public CardView() {

    }

    public CardView(String image) {
        //imageURL = image;
        //this.setImage(image);
        this.image = new Image(image);
        this.setImage(this.image);
        this.setFitWidth(100);
        this.setFitHeight(140);
    }
}
