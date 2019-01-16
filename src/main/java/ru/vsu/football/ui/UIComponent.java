package ru.vsu.football.ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;

public abstract class UIComponent {
    @Getter
    @Setter
    private Scene scene;

    protected Background getBackground() {
        Image image = new Image("IMG_1734.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(ViewResolver.WIDTH, ViewResolver.HEIGHT, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }
}
