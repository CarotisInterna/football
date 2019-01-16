package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.Coach;
import ru.vsu.football.domain.Team;
import ru.vsu.football.service.impl.CoachService;
import ru.vsu.football.service.impl.TeamService;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;
import ru.vsu.football.util.UserContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("Тренеры")
public class CoachComponent extends TableUIComponent<Coach> {

    private static final String WRONG_FIELDS = "Имя от 1 до 30 символов, фамилия от 1 до 40 символов, содержат только русские буквы";
    private static final String WRONG_TEAM = "У команды не может быть 2х тренеров одновременно";
    private CoachService coachService;
    private TeamService teamService;
    private TeamHistoryComponent teamHistoryComponent;

    private ComboBox<Team> teamComboBox;

    @Override
    public void fillTable() {
        super.fillTable();
        ObservableList<Team> teams = FXCollections.observableList(teamService.fetch());
        teamComboBox.setItems(teams);
    }

    @Override
    protected void deleteElement(Coach domain) {
        coachService.delete(domain);
    }

    @Override
    protected void buildTableInternal() {
        final TableColumn<Coach, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Coach, String> surnameColumn = new TableColumn<>("Фамилия");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Coach, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Coach, String> teamColumn = new TableColumn<>("Команда");
        teamColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return Optional.ofNullable(param.getValue().getTeam()).map(Team::getName).orElse("");
            }
        });
        teamColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Coach, Void> historyColumn = new TableColumn<>();
        historyColumn.setCellFactory(showTeamHistoryColumnFactory());

        table.getColumns().addAll(idColumn, surnameColumn, nameColumn, teamColumn, historyColumn);

    }

    @Override
    protected VBox addPane(Coach domain) {
        VBox coachPane = new VBox();

        final TextField nameField = domain == null ? new TextField() : new TextField(domain.getName());
        nameField.setPromptText("Имя");

        final TextField surnameField = domain == null ? new TextField() : new TextField(domain.getSurname());
        nameField.setPromptText("Фамилия");

        final Button saveButton = new Button("Сохранить");

        if (domain != null) {
            teamComboBox.getSelectionModel().select(domain.getTeam());
        } else {
            teamComboBox.getSelectionModel().clearSelection();
        }

        coachPane.getChildren().addAll(nameField, surnameField, teamComboBox, saveButton);

        saveButton.setOnAction(event -> {
            try {
                String fieldTextName = nameField.getText().trim();
                String name = fieldTextName.substring(0, 1).toUpperCase() + fieldTextName.substring(1);
                String fieldTextSurname = surnameField.getText().trim();
                String surname = fieldTextSurname.substring(0, 1).toUpperCase() + fieldTextSurname.substring(1);
                Team selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();
                if (name.length() < 1 || name.length() > 30 || surname.length() < 1 || surname.length() > 40 || !name.matches("[а-яА-Я]+") || !surname.matches("[а-яА-Я]+")) {
                    viewResolver.showError(WRONG_FIELDS);
                } else if (teamHasCoach(domain, selectedTeam)) {
                    viewResolver.showError(WRONG_TEAM);
                } else {

                    Coach coach = Coach.builder()
                            .id(Optional.ofNullable(domain).map(Coach::getId).orElse(null))
                            .name(name)
                            .surname(surname)
                            .team(teamComboBox.getSelectionModel().getSelectedItem())
                            .build();
                    coach = coachService.save(coach);
                    updateView();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });

        return coachPane;
    }

    private boolean teamHasCoach(Coach coach, Team team) {
        if (team == null) return false;
        team = teamService.getById(team.getId());
        if (team.getCoach() == null) return false;
        return !Objects.equals(team.getCoach(),coach);

    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = super.getButtons();
        if (UserContext.isAdmin()) {
            final Button deleteFromTeam = new Button("Убрать из команды");
            deleteFromTeam.setOnAction(event -> {
                Coach domain = table.getSelectionModel().getSelectedItem();
                if (domain != null) {
                    if (domain.getTeam() != null) {
                        domain.setTeam(null);
                        coachService.save(domain);
                        table.refresh();
                    }
                }
            });
            buttons.add(deleteFromTeam);
        }
        return buttons;
    }

    @Override
    protected List<Coach> fetch() {
        return coachService.fetch();
    }

    @Autowired
    public CoachComponent(ViewResolver viewResolver, CoachService coachService, TeamService teamService, TeamHistoryComponent teamHistoryComponent) {
        super(viewResolver);
        this.coachService = coachService;
        this.teamService = teamService;
        this.teamHistoryComponent = teamHistoryComponent;
    }

    @PostConstruct
    void postConstruct() {
        teamComboBox = new ComboBox<>();
        teamComboBox.setCellFactory(cellFactory(Team::getName));
    }

    private Callback<TableColumn<Coach, Void>, TableCell<Coach, Void>> showTeamHistoryColumnFactory() {
        return new Callback<TableColumn<Coach, Void>, TableCell<Coach, Void>>() {
            @Override
            public TableCell<Coach, Void> call(TableColumn<Coach, Void> param) {
                return new TableCell<Coach, Void>() {
                    private String title = "История переходов";
                    private Button btn = new Button(title);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Coach coach = getTableView().getItems().get(getIndex());
                            if (coach != null) {
                                teamHistoryComponent.show(coach.getTeamHistory());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

}
