package ru.vsu.football.ui.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vsu.football.ui.UIComponent;
import ru.vsu.football.ui.ViewResolver;

@Component("Меню")
public class MenuComponent extends UIComponent {
    private ViewResolver viewResolver;


    @Autowired
    public MenuComponent(final ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
        final VBox vBox = new VBox();
        vBox.setPrefWidth(ViewResolver.WIDTH);
        vBox.setPrefHeight(ViewResolver.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        GridPane pane = new GridPane();
        this.setScene(new Scene(vBox));


        vBox.getChildren().addAll(pane);

        Text title = new Text("Меню");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));

        final Button citiesButton = createButton("Города", viewResolver::showCities);

        final Button playersButton = createButton("Игроки", viewResolver::showPlayers);

        final Button coachesButton = createButton("Тренеры", viewResolver::showCoaches);

        final Button stadiumButton = createButton("Стадионы", viewResolver::showStadiums);

        final Button teamButton = createButton("Команды", viewResolver::showTeams);

        final Button matchButton = createButton("Матчи", viewResolver::showMatches);

        final Button exitButton = createButton("Выход", viewResolver::showLoginOnExit);

        pane.add(title, 15, 0);
        pane.add(citiesButton, 0, 2);
        pane.add(stadiumButton, 0, 4);
        pane.add(matchButton, 0, 6);
        pane.add(coachesButton, 0, 8);
        pane.add(teamButton, 0, 10);
        pane.add(playersButton, 0, 12);
        pane.add(exitButton, 30, 16);

        pane.setAlignment(Pos.CENTER);

        pane.setHgap(10);

        pane.setVgap(15);


        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.LEFT);
        pane.getColumnConstraints().add(column1);
//
//        ColumnConstraints column2 = new ColumnConstraints();
//        column2.setHalignment(HPos.LEFT);
//        pane.getColumnConstraints().add(column2);


        vBox.setBackground(getBackground());
    }


    private Button createButton(String name, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(name);

        button.setScaleX(2);
        button.setScaleY(2);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(eventHandler);

        button.setTextFill(Color.FIREBRICK);
        return button;
    }

}