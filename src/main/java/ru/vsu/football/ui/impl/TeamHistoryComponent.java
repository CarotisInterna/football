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
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.TeamHistory;
import ru.vsu.football.ui.UIComponent;

import java.util.Date;
import java.util.List;

@Component
public class TeamHistoryComponent extends UIComponent {
    private TableView<TeamHistory> tableView;

    public TeamHistoryComponent() {
        VBox history = new VBox();
        tableView = new TableView<>();
        TableColumn<TeamHistory, String> teamColumn = new TableColumn<>("Команда");
        teamColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getTeam().getName();
            }
        });
        TableColumn<TeamHistory, String> actionTypeColumn = new TableColumn<>("Действие");
        actionTypeColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getActionType().getName();
            }
        });
        TableColumn<TeamHistory, Date> actionDateColumn = new TableColumn<>("Дата");
        actionDateColumn.setCellValueFactory(new PropertyValueFactory<>("actionDate"));
        actionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));

        history.getChildren().addAll(tableView);
        tableView.getColumns().addAll(teamColumn, actionTypeColumn, actionDateColumn);
        setScene(new Scene(history));
    }

    public void show(List<TeamHistory> list) {
        tableView.getItems().clear();
        if (list != null) {
            tableView.getItems().addAll(list);
        }
        Stage secondStage = new Stage();

        secondStage.setScene(getScene());
        secondStage.show();
    }
}
