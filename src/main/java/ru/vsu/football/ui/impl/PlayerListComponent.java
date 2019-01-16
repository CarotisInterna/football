package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.Player;
import ru.vsu.football.domain.TeamHistory;
import ru.vsu.football.ui.UIComponent;

import java.util.Date;
import java.util.List;

@Component
public class PlayerListComponent extends UIComponent {
    private TableView<Player> tableView;

    public PlayerListComponent() {
        VBox history = new VBox();
        tableView = new TableView<>();
        final TableColumn<Player, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Player, String> surnameColumn = new TableColumn<>("Фамилия");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Player, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Player, Integer> ageColumn = new TableColumn<>("Возраст");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Player, String> cityColumn = new TableColumn<>("Город");
        cityColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getCity().getName();
            }
        });
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Player, String> roleColumn = new TableColumn<>("Роль");
        roleColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getRole().getName();
            }
        });

        history.getChildren().addAll(tableView);
        tableView.getColumns().addAll(idColumn, surnameColumn, nameColumn, ageColumn, cityColumn, roleColumn);
        setScene(new Scene(history));
    }

    public void show(List<Player> list) {
        tableView.getItems().clear();
        if (list != null) {
            tableView.getItems().addAll(list);
        }
        Stage secondStage = new Stage();

        secondStage.setScene(getScene());
        secondStage.show();
    }
}
