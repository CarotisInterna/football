package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
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
import ru.vsu.football.domain.City;
import ru.vsu.football.domain.Team;
import ru.vsu.football.service.impl.CityService;
import ru.vsu.football.service.impl.TeamService;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Component("Команды")
public class TeamComponent extends TableUIComponent<Team> {

    private static final String WRONG_FIELDS = "Название от 1 до 30 символов, содержит только русские буквы. Предыдущее место неотрицательно и меньше 30";
    private PlayerListComponent playerListComponent;
    private TeamService teamService;
    private CityService cityService;
    private ComboBox<City> cityComboBox;

    @Override
    public void fillTable() {
        super.fillTable();
        cityComboBox.setItems(FXCollections.observableList(cityService.fetch()));
    }

    @Override
    protected void deleteElement(Team domain) {
        teamService.delete(domain);
    }

    @Override
    protected void buildTableInternal() {

        final TableColumn<Team, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Team, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Team, Integer> prevPlaceColumn = new TableColumn<>("Предыдущее место");
        prevPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("prevPlace"));
        prevPlaceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Team, String> cityColumn = new TableColumn<>("Город");
        cityColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getCity().getName();
            }
        });
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Team, Void> showPlayersColumn = new TableColumn<>();
        showPlayersColumn.setCellFactory(showPlayerListCellFactory());

        table.getColumns().addAll(idColumn, nameColumn, prevPlaceColumn, cityColumn, showPlayersColumn);

    }

    @Override
    protected VBox addPane(Team domain) {
        VBox teamPane = new VBox();
        final TextField nameField = domain == null ? new TextField() : new TextField(domain.getName());
        nameField.setPromptText("Название");
        final TextField prevPlaceField = new TextField(Optional.ofNullable(domain).map(Team::getPrevPlace).map(Object::toString).orElse(""));
        prevPlaceField.setPromptText("Предыдущее место");
        final Button saveButton = new Button("Сохранить");
        if (domain != null) {
            cityComboBox.getSelectionModel().select(domain.getCity());
        } else {
            cityComboBox.getSelectionModel().selectFirst();
        }
        teamPane.getChildren().addAll(nameField, prevPlaceField, cityComboBox, saveButton);
        saveButton.setOnAction(event -> {
            try {
                String fieldText = nameField.getText().trim();
                String name = fieldText.substring(0, 1).toUpperCase() + fieldText.substring(1);

                Integer prevPlace = prevPlaceField.getText().trim().isEmpty() ? null : Integer.valueOf(prevPlaceField.getText().trim());
                if (name.length() < 1 || name.length() > 30 || !name.matches("[а-яА-Я\\- ]+")
                        || (prevPlace != null && prevPlace < 1 && prevPlace > 30)) {
                    viewResolver.showError(WRONG_FIELDS);
                } else {
                    Team team = Team.builder()
                            .id(Optional.ofNullable(domain).map(Team::getId).orElse(null))
                            .prevPlace(prevPlace)
                            .name(name)
                            .city(cityComboBox.getSelectionModel().getSelectedItem())
                            .build();
                    team = teamService.save(team);
                    if (domain == null) {
                        table.getItems().add(team);
                    }
                    updateView();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            } catch (Throwable e) {
                System.out.println();
            }
        });
        return teamPane;
    }

    @Override
    protected List<Team> fetch() {
        return teamService.fetch();
    }

    @Autowired
    public TeamComponent(ViewResolver viewResolver, PlayerListComponent playerListComponent, TeamService teamService, CityService cityService) {
        super(viewResolver);
        this.playerListComponent = playerListComponent;
        this.teamService = teamService;
        this.cityService = cityService;
    }

    @PostConstruct
    void postConstruct() {
        cityComboBox = new ComboBox<>();
        cityComboBox.setCellFactory(cellFactory(City::getName));
    }

    private Callback<TableColumn<Team, Void>, TableCell<Team, Void>> showPlayerListCellFactory() {
        return new Callback<TableColumn<Team, Void>, TableCell<Team, Void>>() {
            @Override
            public TableCell<Team, Void> call(TableColumn<Team, Void> param) {
                return new TableCell<Team, Void>() {
                    private String title = "Состав";
                    private Button btn = new Button(title);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Team team = getTableView().getItems().get(getIndex());
                            if (team != null) {
                                playerListComponent.show(team.getPlayers());
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
