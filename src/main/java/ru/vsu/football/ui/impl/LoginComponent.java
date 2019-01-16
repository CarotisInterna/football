package ru.vsu.football.ui.impl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import ru.vsu.football.service.LoginService;
import ru.vsu.football.ui.UIComponent;
import ru.vsu.football.ui.ViewResolver;

@Component("Вход")
public class LoginComponent extends UIComponent {
    private ViewResolver viewResolver;
    private LoginService loginService;

    public LoginComponent(ViewResolver viewResolver, LoginService loginService) {
        this.viewResolver = viewResolver;
        this.loginService = loginService;

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        setScene(new Scene(grid, ViewResolver.WIDTH, ViewResolver.HEIGHT));
        Text title = new Text("Добро пожаловать");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Label usernameLabel = new Label("Имя пользователя:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Войти");
        Label registerLabel = new Label("Нет аккаунта?");
        Button registerButton = new Button("Зарегистрироваться");
        grid.add(title, 0, 0, 2, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameTextField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
        grid.add(registerLabel, 0, 4);
        grid.add(registerButton, 1, 4);
        loginButton.setOnAction(event -> {
            try {
                String user = usernameTextField.getCharacters().toString();
                String password = passwordField.getCharacters().toString();
                if (user.isEmpty() || password.isEmpty() || user.trim().isEmpty() || password.trim().isEmpty()) {
                    throw new IllegalArgumentException("Поле не заполнено");
                }
                this.loginService.login(user, password);
                viewResolver.showMenu();
            } catch (IllegalArgumentException ex) {
                viewResolver.showError(ex.getMessage());
            }
        });
        registerButton.setOnAction(e-> viewResolver.showRegistration());
    }

}
