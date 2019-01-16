package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.football.domain.Match;
import ru.vsu.football.domain.Stadium;
import ru.vsu.football.domain.Team;
import ru.vsu.football.domain.Ticket;
import ru.vsu.football.service.impl.MatchService;
import ru.vsu.football.service.impl.StadiumService;
import ru.vsu.football.service.impl.TeamService;
import ru.vsu.football.ui.ManagableUIComponent;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;
import ru.vsu.football.util.UserContext;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component("Матчи")
public class MatchComponent extends TableUIComponent<Match> implements ManagableUIComponent {
    private static final String WRONG_FIELDS = "Название от 0 до 255 символов";
    private static final String WRONG_RESULTS_POSITIVE = "Гол команды должен быть целым неотрицательным числом";
    private static final String WRONG_RESULTS_15 = "Команда не может забить больше 15 голов";
    private static final String WRONG_RESULTS_NOT_NULL = "Одно из мест команд не может быть пустым";
    private static final String WRONG_RESULTS_DATE = "Матч еще не состоялся, голы не могли быть забиты";
    private static final String WRONG_DATE = "Дата не может быть раньше текущей";
    private static final String WRONG_ACTION_WITH_DATE = "Нельзя передвинуть матч на дату, раньше вчерашнего дня, или с имеющимися результатами";
    private static final String WRONG_TEAMS = "Команды должны быть разными";
    private static final String WRONG_NOT_STAYED_MATCH = "Нельзя перенести в прошлое матч, у которого нет счета";
    private MatchService matchService;
    private StadiumService stadiumService;
    private TeamService teamService;
    private ComboBox<Team> team1ComboBox;
    private ComboBox<Team> team2ComboBox;
    private ComboBox<Stadium> stadiumComboBox;

    @Override
    public void fillTable() {
        super.fillTable();
        stadiumComboBox.setItems(FXCollections.observableList(stadiumService.fetch()));
        ObservableList<Team> teams = FXCollections.observableList(teamService.fetch());
        team1ComboBox.setItems(teams);
        team2ComboBox.setItems(teams);
    }

    @Override
    protected void deleteElement(Match domain) {
        matchService.delete(domain);
    }

    @Override
    protected void buildTableInternal() {
        final TableColumn<Match, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Match, String> stadiumColumn = new TableColumn<>("Стадион");
        stadiumColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getStadium().getName();
            }
        });

        final TableColumn<Match, Date> dateColumn = new TableColumn<>("Дата матча");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));

        final TableColumn<Match, String> team1Column = new TableColumn<>("Хозяин");
        team1Column.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getTeam1().getName();
            }
        });

        final TableColumn<Match, String> team2Column = new TableColumn<>("Гость");
        team2Column.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getTeam2().getName();
            }
        });

        final TableColumn<Match, String> ticketColumn = new TableColumn<>("Цена билета");
        ticketColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return Optional.ofNullable(param.getValue())
                        .map(Match::getTicket)
                        .map(Ticket::getPrice)
                        .map(Object::toString)
                        .orElse("");
            }
        });

        final TableColumn<Match, Integer> team_1_resColumn = new TableColumn<>("Голы хозяина");
        team_1_resColumn.setCellValueFactory(new PropertyValueFactory<>("team1Res"));
        team_1_resColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Match, Integer> team_2_resColumn = new TableColumn<>("Голы гостя");
        team_2_resColumn.setCellValueFactory(new PropertyValueFactory<>("team2Res"));
        team_2_resColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        stadiumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        table.getColumns().addAll(idColumn, stadiumColumn, dateColumn, team1Column, team2Column, team_1_resColumn, team_2_resColumn, ticketColumn);

    }

    @Override
    @Transactional
    protected VBox addPane(Match domain) {
        VBox matchPane = new VBox();
        DatePicker datePicker = new DatePicker(domain == null ? LocalDate.now() : toLocalDate(domain.getDate()));
        Optional<Integer> team1Res = Optional.ofNullable(domain).map(Match::getTeam1Res);
        final TextField team1ResField = team1Res.isPresent() ? new TextField(domain.getTeam1Res().toString()) : new TextField();
        team1ResField.setPromptText("Голы команды 1");
        Optional<Integer> team2Res = Optional.ofNullable(domain).map(Match::getTeam2Res);
        final TextField team2ResField = team2Res.isPresent() ? new TextField(domain.getTeam2Res().toString()) : new TextField();
        team2ResField.setPromptText("Голы команды 2");

        final TextField ticketPriceField = new TextField(Optional.ofNullable(domain)
                .map(Match::getTicket).map(Ticket::getPrice).map(Object::toString).orElse(""));
        ticketPriceField.setPromptText("Цена билета");
        final Button saveButton = new Button("Сохранить");

        if (domain != null) {
            stadiumComboBox.getSelectionModel().select(domain.getStadium());
            team1ComboBox.getSelectionModel().select(domain.getTeam1());
            team2ComboBox.getSelectionModel().select(domain.getTeam2());
        } else {
            stadiumComboBox.getSelectionModel().selectFirst();
            team1ComboBox.getSelectionModel().selectFirst();
            team2ComboBox.getSelectionModel().selectFirst();
        }

        matchPane.getChildren().addAll(datePicker);

        if (UserContext.isAdmin()) {
            matchPane.getChildren().addAll(stadiumComboBox, team1ComboBox, team2ComboBox, team1ResField, team2ResField);
        }

        matchPane.getChildren().addAll(saveButton);

        saveButton.setOnAction(event -> {
            try {
                Integer team1Result = team1ResField.getText().trim().isEmpty() ? null : Integer.valueOf(team1ResField.getText().trim());
                Integer team2Result = team2ResField.getText().trim().isEmpty() ? null : Integer.valueOf(team2ResField.getText().trim());
                Team team1 = team1ComboBox.getSelectionModel().getSelectedItem();
                Team team2 = team2ComboBox.getSelectionModel().getSelectedItem();

//                Ticket ticket = Optional.of(ticketPriceField.getText()).map(Double::valueOf)
//                        .map(value -> Ticket.builder().price(value).build()).orElse(null);


                LocalDate matchDate = datePicker.getValue();
                if (Objects.equals(team1.getName(), team2.getName())) {
                    viewResolver.showError(WRONG_TEAMS);
                } else if (
                        (team1Result == null && team2Result != null)
                                || (team1Result != null && team2Result == null)) {
                    viewResolver.showError(WRONG_RESULTS_NOT_NULL);
                } else if (team1Result != null && team2Result != null && team1Result < 0 && team2Result < 0) {
                    viewResolver.showError(WRONG_RESULTS_POSITIVE);
                } else if (team1Result != null && team2Result != null &&team1Result > 15 && team2Result > 15) {
                    viewResolver.showError(WRONG_RESULTS_15);
                } else if (team1Result != null && team2Result != null && matchDate.isAfter(LocalDate.now())) {
                    viewResolver.showError(WRONG_RESULTS_DATE);
                } else if (team1Result == null && team2Result == null && matchDate.isBefore(LocalDate.now())) {
                    viewResolver.showError(WRONG_NOT_STAYED_MATCH);
                } else {

                    Match match = Match.builder()
                            .id(Optional.ofNullable(domain).map(Match::getId).orElse(null))
                            .date(toDate(matchDate))
                            .team1Res(team1Result)
                            .team2Res(team2Result)
                            .team1(team1)
                            .team2(team2)
//                            .ticket()
                            .stadium(stadiumComboBox.getSelectionModel().getSelectedItem())
                            .build();

                    match = matchService.save(match);

                    updateView();


                }
            } catch (NumberFormatException ex) {
                viewResolver.showError(WRONG_RESULTS_POSITIVE);
            } catch (DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
                log.error("{}", ex);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return matchPane;
    }

    @Override
    protected List<Match> fetch() {
        return matchService.fetch();
    }

    @Autowired
    public MatchComponent(ViewResolver viewResolver,
                          MatchService matchService, TeamService teamService, StadiumService stadiumService) {
        super(viewResolver);
        this.matchService = matchService;
        this.teamService = teamService;
        this.stadiumService = stadiumService;
    }

    @PostConstruct
    void postConstruct() {
        stadiumComboBox = new ComboBox<>();
        stadiumComboBox.setCellFactory(cellFactory(Stadium::getName));
        team1ComboBox = new ComboBox<>();
        team2ComboBox = new ComboBox<>();
        team1ComboBox.setCellFactory(cellFactory(Team::getName));
        team2ComboBox.setCellFactory(cellFactory(Team::getName));
    }

    Date toDate(LocalDate localDate) {
        return Date.from(Instant.from(localDate.atStartOfDay(ZoneId.systemDefault())));
    }

    LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public List<Button> getManagerButtons() {
        Button changeDateButton = new Button("Изменить дату");
        changeDateButton.setOnAction(e -> {
            try {
                Match domain = table.getSelectionModel().getSelectedItem();
                if (domain != null) {
                    if (domain.getDate().after(toDate(LocalDate.now().minusDays(1))) && !domain.hasResults()) {
                        viewResolver.showPaneDialog("Изменить дату", addPane(domain));
                    } else {
                        viewResolver.showError(WRONG_ACTION_WITH_DATE);
                    }
                }
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return Arrays.asList(changeDateButton);
    }
}


//1Брать в команду первый молодой свободный
//2Брать первого свободного тренера, который не тренеровал команду раньше
//добавление и удаление даты


//3стадион не может принять матч, если в этот день или за день до стояла игра - DONE
//4цена - DONE
//7команда не может переезжать в город, если там нет стадиона - DONE
//8команда, которая выиграла 5 игр не может играть в стадионе с вместимостью меньше 1000 - DONE
//9комплектность команды - DONE
//создание записи в истории при создании игрока и обновлении команды
//создание записи в истории при создании тренера и обновлении команды
//существующий город
//существующий стадион в этом городе
