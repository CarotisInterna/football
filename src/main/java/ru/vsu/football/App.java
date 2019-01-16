package ru.vsu.football;

import javafx.application.Preloader;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.football.ui.ViewResolver;

@SpringBootApplication
@SuppressWarnings("restriction")
public class App extends AbstractJavaFxApplicationSupport {
    @Value("${app.ui.title:Example App}")
    private String windowTitle;

    @Autowired
    private ViewResolver viewResolver;

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
        viewResolver.setPrimaryStage(stage);
        stage.setTitle(windowTitle);
        viewResolver.showLogin();
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(App.class, args);
    }

}