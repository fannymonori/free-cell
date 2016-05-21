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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author fanny
 */
public class FreeCellView extends ImageView{
    
    public Image backgroundImage;

    public FreeCellView(String image) {
        
        String cssStyle = "-fx-border-color: black;\n"
                + "-fx-border-width: 3;\n";
        this.setImage(new Image(image));
        
        this.setFitWidth(100);
        this.setFitHeight(140);
        
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }
    
}
