package ru.vsu.football.ui.impl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import ru.vsu.football.service.LoginService;
import ru.vsu.football.ui.UIComponent;
import ru.vsu.football.ui.ViewResolver;

@Component("Регистрация")
public class RegistrationComponent extends UIComponent {
    private ViewResolver viewResolver;
    private LoginService loginService;

    public RegistrationComponent(ViewResolver viewResolver, LoginService loginService) {
        this.viewResolver = viewResolver;
        this.loginService = loginService;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        this.setScene(new Scene(grid, ViewResolver.WIDTH, ViewResolver.HEIGHT));
        Text title = new Text("Добро пожаловать");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Label usernameLabel = new Label("Имя пользователя:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("Пароль:");
        Label confirmPasswordLabel = new Label("Подтвердите пароль:");
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Button registerButton = new Button("Зарегистрироваться");
        Label loginLabel = new Label("Уже есть аккаунт?");
        Button loginButton = new Button("Войти");
        grid.add(title, 0, 0, 2, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameTextField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(confirmPasswordLabel, 0, 3);
        grid.add(confirmPasswordField, 1, 3);
        grid.add(registerButton, 0, 4);
        grid.add(loginButton, 0, 5);
        grid.add(loginLabel, 1, 5);
        loginButton.setOnAction(event -> viewResolver.showLogin());
        registerButton.setOnAction(event -> {
            try {
                String user = usernameTextField.getCharacters().toString();
                String password = passwordField.getCharacters().toString();
                String confirmPassword = confirmPasswordField.getCharacters().toString();
                if (user.trim().isEmpty() || password.trim().isEmpty()
                        || confirmPassword.trim().isEmpty()) {
                    throw new IllegalArgumentException("Поле не может быть пустым");
                }
                if (user.length() > 255
                        || password.length() > 255 || confirmPassword.length() > 255) {
                    throw new IllegalArgumentException("Значение в текстовом поле не может быть длиннее 255 символов");
                }
                if (user.length() < 5) {
                    throw new IllegalArgumentException("Имя пользователя должно быть длиннее 5 символов");
                }
                if (!password.trim().equals(confirmPassword.trim())) {
                    throw new IllegalArgumentException("Пароли не совпадают");
                }
                this.loginService.register(user, password);
                this.viewResolver.showMenu();
            } catch (Exception ex) {
                this.viewResolver.showError(ex.getMessage());
            }
        });
    }
}
