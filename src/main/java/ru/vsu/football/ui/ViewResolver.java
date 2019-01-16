package ru.vsu.football.ui;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.TeamHistory;
import ru.vsu.football.util.UserContext;

import java.util.List;

@Service
public class ViewResolver {
    public final static int WIDTH = 900, HEIGHT = 506;
    private UIComponentFactory factory;
    private Stage primaryStage;
    private Scene previousScene;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMenu() {
        showMenu(null);
    }

    public void showMenu(ActionEvent event) {
        showComponent("Меню");
    }

    private void showComponent(String name) {
        previousScene = primaryStage.getScene();
        UIComponent component = factory.getUIComponent(name);
        if (component instanceof TableUIComponent) {
            ((TableUIComponent) (component)).configure();
            ((TableUIComponent) (component)).fillTable();
        }
        primaryStage.setScene(component.getScene());
    }

    public void showCities(ActionEvent event) {
        showComponent("Города");
    }

    public void showPreviousScene(ActionEvent event) {
        primaryStage.setScene(previousScene);
        previousScene = null;
    }

    public void showPaneDialog(String title, VBox pane) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(title);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CLOSE);
        alert.getDialogPane().setContent(pane);
        alert.show();
    }

    public void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setResizable(true);
        alert.setTitle("");
        alert.setHeaderText("");
        Label label = new Label("Сообщение:");
        TextArea textArea = new TextArea(error);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    public void showPlayers(ActionEvent event) {
        showComponent("Игроки");
    }

    public void showCoaches(ActionEvent event) {
        showComponent("Тренеры");
    }

    public void showStadiums(ActionEvent event) {
        showComponent("Стадионы");
    }

    public void showTeams(ActionEvent event) {
        showComponent("Команды");
    }

    public void showMatches(ActionEvent event) {
        showComponent("Матчи");
    }

    public void showLogin() {
        showComponent("Вход");
    }

    public void showRegistration() {
        showComponent("Регистрация");
    }

    public void showLoginOnExit(ActionEvent event) {
        UserContext.setRole(null);
        showLogin();
    }



    @Autowired
    public void setFactory(UIComponentFactory factory) {
        this.factory = factory;
    }
}
