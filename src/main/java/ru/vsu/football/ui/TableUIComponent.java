package ru.vsu.football.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.vsu.football.util.UserContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class TableUIComponent<DMN> extends UIComponent {
    protected TableView<DMN> table;
    protected ViewResolver viewResolver;
    private boolean isConfigured = false;


    protected TableUIComponent(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public void configure() {
        isConfigured = true;
        buildTable();

        final VBox vBox = new VBox();
        final AnchorPane anchorPane = new AnchorPane();
        final HBox hBox = new HBox();
        vBox.setPrefWidth(ViewResolver.WIDTH);
        vBox.setPrefHeight(ViewResolver.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER);
        GridPane pane = new GridPane();
        pane.add(table, 0, 0);
        this.setScene(new Scene(vBox));


        ButtonBar bar = new ButtonBar();

        bar.getButtons().addAll(getButtons());

        anchorPane.getChildren().addAll(pane);
        hBox.getChildren().addAll(bar);
        vBox.getChildren().addAll(anchorPane, hBox);
        vBox.setBackground(getBackground());

    }

    private void buildTable() {
        this.table = new TableView<>();
        table.setPrefWidth(ViewResolver.WIDTH);
        table.setPrefHeight(ViewResolver.HEIGHT);
        buildTableInternal();
    }

    protected abstract void deleteElement(DMN domain);

    protected abstract void buildTableInternal();

    protected abstract VBox addPane(DMN domain);

    protected abstract List<DMN> fetch();

    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        switch (UserContext.getUserRole()) {
            case ADMIN: {
                buttons.addAll(getAdminButtons());
                break;
            }
            case MANAGER: {
                if (isManagable()) {
                    buttons.addAll(((ManagableUIComponent)(this)).getManagerButtons());
                }
                break;
            }
        }
        buttons.addAll(getUserButtons());
        return buttons;
    }

    protected void updateView(){
        fetch();
        fillTable();
        table.refresh();
    }

    private List<Button> getUserButtons() {
        final Button goToPreviousSceneButton = new Button("Назад");
        goToPreviousSceneButton.setOnAction(viewResolver::showPreviousScene);
        return Collections.singletonList(goToPreviousSceneButton);
    }

    private List<Button> getAdminButtons() {
        final Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> viewResolver.showPaneDialog("Добавить", addPane(null)));

        final Button changeButton = new Button("Изменить");
        final Button deleteButton = new Button("Удалить");


        deleteButton.setOnAction(e -> {
            DMN domain = table.getSelectionModel().getSelectedItem();
            if (domain != null) {
                deleteElement(domain);
            }
            updateView();
        });

        changeButton.setOnAction(e -> {
            DMN dto = table.getSelectionModel().getSelectedItem();
            if (dto != null) {
                viewResolver.showPaneDialog("Изменить", addPane(dto));
            }
        });
        return Arrays.asList(addButton, changeButton, deleteButton);

    }


    public void fillTable() {
        ObservableList<DMN> objects = FXCollections.observableArrayList(fetch());
        table.setItems(objects);
    }

    protected <T> Callback<ListView<T>, ListCell<T>> cellFactory(Function<T, String> extractor) {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(extractor.apply(item));
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        };
    }

    private boolean isManagable() {
        return this instanceof ManagableUIComponent;
    }
}
