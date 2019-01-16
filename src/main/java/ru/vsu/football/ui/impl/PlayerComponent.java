package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.City;
import ru.vsu.football.domain.Player;
import ru.vsu.football.domain.Role;
import ru.vsu.football.domain.Team;
import ru.vsu.football.service.impl.CityService;
import ru.vsu.football.service.impl.PlayerService;
import ru.vsu.football.service.impl.RoleService;
import ru.vsu.football.service.impl.TeamService;
import ru.vsu.football.ui.ManagableUIComponent;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;
import ru.vsu.football.util.UserContext;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component("Игроки")
public class PlayerComponent extends TableUIComponent<Player> implements ManagableUIComponent {
    private static final String WRONG_FIELDS = "Имя от 1 до 30 символов, фамилия от 1 до 40 символов, содержат только русские буквы. Игрок должен быть старше 18 лет";
    private PlayerService playerService;
    private CityService cityService;
    private RoleService roleService;
    private TeamService teamService;

    private TeamHistoryComponent teamHistoryComponent;
    private ComboBox<City> cityComboBox;
    private ComboBox<Role> roleComboBox;
    private ComboBox<Team> teamComboBox;

    @Override
    public void fillTable() {
        super.fillTable();
        ObservableList<City> cities = FXCollections.observableList(cityService.fetch());
        ObservableList<Role> roles = FXCollections.observableList(roleService.fetch());
        ObservableList<Team> teams = FXCollections.observableList(teamService.fetch());
        cityComboBox.setItems(cities);
        roleComboBox.setItems(roles);
        teamComboBox.setItems(teams);
    }

    @Override
    protected void deleteElement(Player domain) {
        playerService.delete(domain);
    }

    @Override
    protected void buildTableInternal() {
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

        final TableColumn<Player, String> teamColumn = new TableColumn<>("Команда");
        teamColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return Optional.ofNullable(param.getValue().getTeam()).map(Team::getName).orElse("");
            }
        });

        TableColumn<Player, Void> historyColumn = new TableColumn<>();
        historyColumn.setCellFactory(showTeamHistoryColumnFactory());


        table.getColumns().addAll(idColumn, surnameColumn, nameColumn, ageColumn, cityColumn, roleColumn, teamColumn, historyColumn);

    }


    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = super.getButtons();
        final Button deleteFromTeam = new Button("Убрать из команды");
        deleteFromTeam.setOnAction(event -> {
            Player domain = table.getSelectionModel().getSelectedItem();
            if (domain == null) {
                viewResolver.showError("Не выбран элемент");
            } else {
                if (domain.getTeam() != null) {
                    domain.setTeam(null);
                    playerService.save(domain);
                    fetch();
                    fillTable();
                    table.refresh();
                }
            }
        });
        buttons.add(deleteFromTeam);
        return buttons;
    }

    @Override
    protected VBox addPane(Player domain) {
        VBox playerPane = new VBox();
        final TextField nameField = domain == null ? new TextField() : new TextField(domain.getName());
        nameField.setPromptText("Имя");
        final TextField surnameField = domain == null ? new TextField() : new TextField(domain.getSurname());
        surnameField.setPromptText("Фамилия");
        final TextField ageField = domain == null ? new TextField() : new TextField(domain.getAge().toString());
        ageField.setPromptText("Возраст");

        final Button saveButton = new Button("Сохранить");

        if (domain != null) {
            cityComboBox.getSelectionModel().select(domain.getCity());
            roleComboBox.getSelectionModel().select(domain.getRole());
            teamComboBox.getSelectionModel().select(domain.getTeam());
        } else {
            cityComboBox.getSelectionModel().selectFirst();
            roleComboBox.getSelectionModel().selectFirst();
            teamComboBox.getSelectionModel().clearSelection();
        }

        if (UserContext.isManager()) {
            nameField.setEditable(false);
            surnameField.setEditable(false);
            ageField.setEditable(false);
        }

        playerPane.getChildren().addAll(nameField, surnameField, ageField);
        if (UserContext.isAdmin()) {
            playerPane.getChildren().add(cityComboBox);
        }
        playerPane.getChildren().addAll(roleComboBox, teamComboBox, saveButton);

        saveButton.setOnAction(event -> {
            try {
                String fieldTextName = nameField.getText().trim();
                String name = fieldTextName.substring(0, 1).toUpperCase() + fieldTextName.substring(1);
                String fieldTextSurname = surnameField.getText().trim();
                String surname = fieldTextSurname.substring(0, 1).toUpperCase() + fieldTextSurname.substring(1);
                Integer age = Integer.valueOf(ageField.getText().trim());
                if (name.length() < 1 || name.length() > 30 || surname.length() < 1 || surname.length() > 40 || !name.matches("[а-яА-Я]+") || !surname.matches("[а-яА-Я]+") || age < 18) {
                    viewResolver.showError(WRONG_FIELDS);
                } else {

                    Player player = Player.builder()
                            .id(Optional.ofNullable(domain).map(Player::getId).orElse(null))
                            .name(name)
                            .surname(surname)
                            .age(age)
                            .city(cityComboBox.getSelectionModel().getSelectedItem())
                            .role(roleComboBox.getSelectionModel().getSelectedItem())
                            .team(teamComboBox.getSelectionModel().getSelectedItem())
                            .build();
                    player = playerService.save(player);
                    updateView();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return playerPane;
    }

    @Override
    protected List<Player> fetch() {
        return playerService.fetch();
    }

    @Autowired
    public PlayerComponent(ViewResolver viewResolver,
                           PlayerService playerService, CityService cityService, RoleService roleService, TeamService teamService, TeamHistoryComponent teamHistoryComponent) {
        super(viewResolver);
        this.playerService = playerService;
        this.cityService = cityService;
        this.roleService = roleService;
        this.teamService = teamService;
        this.teamHistoryComponent = teamHistoryComponent;
    }


    @PostConstruct
    void postConstruct() {
        cityComboBox = new ComboBox<>();
        cityComboBox.setCellFactory(cellFactory(City::getName));
        roleComboBox = new ComboBox<>();
        roleComboBox.setCellFactory(cellFactory(Role::getName));
        teamComboBox = new ComboBox<>();
        teamComboBox.setCellFactory(cellFactory(Team::getName));
    }

    @Override
    public List<Button> getManagerButtons() {
        Button setTeamButton = new Button("Изменить команду");
        setTeamButton.setOnAction(e -> {
            try {
                Player domain = table.getSelectionModel().getSelectedItem();
                if (domain != null) {
                    viewResolver.showPaneDialog("Изменить команду", addPane(domain));
                }
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return Arrays.asList(setTeamButton);
    }


    private Callback<TableColumn<Player, Void>, TableCell<Player, Void>> showTeamHistoryColumnFactory() {
        return new Callback<TableColumn<Player, Void>, TableCell<Player, Void>>() {
            @Override
            public TableCell<Player, Void> call(TableColumn<Player, Void> param) {
                return new TableCell<Player, Void>() {
                    private String title = "История переходов";
                    private Button btn = new Button(title);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Player player = getTableView().getItems().get(getIndex());
                            if (player != null) {
                                teamHistoryComponent.show(player.getTeamHistory());
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
