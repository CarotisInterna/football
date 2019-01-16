package ru.vsu.football.ui.impl;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.City;
import ru.vsu.football.domain.Stadium;
import ru.vsu.football.service.impl.CityService;
import ru.vsu.football.service.impl.StadiumService;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Component("Стадионы")
public class StadiumComponent extends TableUIComponent<Stadium> {
    private static final String WRONG_FIELDS = "Название от 1 до 40 символов, содержит только русские буквы. Вместимость от 100 до 100000";
    private StadiumService stadiumService;
    private CityService cityService;
    private ComboBox<City> cityComboBox;

    @Override
    public void fillTable() {
        super.fillTable();
        cityComboBox.setItems(FXCollections.observableList(cityService.fetch()));
    }

    @Override
    protected void deleteElement(Stadium domain) {
        stadiumService.delete(domain);
    }

    @Override
    protected void buildTableInternal() {
        final TableColumn<Stadium, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<Stadium, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<Stadium, Long> capacityColumn = new TableColumn<>("Вместимость");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));

        final TableColumn<Stadium, String> cityColumn = new TableColumn<>("Город");
        cityColumn.setCellValueFactory(param -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return param.getValue().getCity().getName();
            }
        });

        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        table.getColumns().addAll(idColumn, nameColumn, capacityColumn, cityColumn);

    }

    @Override
    protected VBox addPane(Stadium domain) {
        VBox stadiumPane = new VBox();
        final TextField nameField = domain == null ? new TextField() : new TextField(domain.getName());
        nameField.setPromptText("Название");
        final TextField capacityField = domain == null ? new TextField() : new TextField(domain.getCapacity().toString());
        capacityField.setPromptText("Вместимость");
        final Button saveButton = new Button("Сохранить");
        if (domain != null) {
            cityComboBox.getSelectionModel().select(domain.getCity());
        } else {
            cityComboBox.getSelectionModel().selectFirst();
        }
        stadiumPane.getChildren().addAll(nameField, capacityField, cityComboBox, saveButton);
        saveButton.setOnAction(event -> {
            try {
                String fieldText = nameField.getText().trim();
                String name = fieldText.substring(0, 1).toUpperCase() + fieldText.substring(1);
                Long capacity = Long.valueOf(capacityField.getText().trim());
                if (name.length() < 1 || name.length() > 40 || !name.matches("[а-яА-Я]+") || capacity < 100 || capacity > 100000) {
                    viewResolver.showError(WRONG_FIELDS);
                } else {
                    Stadium stadium = Stadium.builder()
                            .id(Optional.ofNullable(domain).map(Stadium::getId).orElse(null))
                            .capacity(capacity)
                            .name(name)
                            .city(cityComboBox.getSelectionModel().getSelectedItem())
                            .build();
                    stadium = stadiumService.save(stadium);
                    if (domain == null) {
                        table.getItems().add(stadium);
                    }
                    updateView();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return stadiumPane;
    }

    @Override
    protected List<Stadium> fetch() {
        return stadiumService.fetch();
    }

    @Autowired
    public StadiumComponent(ViewResolver viewResolver, StadiumService stadiumService, CityService cityService) {
        super(viewResolver);
        this.stadiumService = stadiumService;
        this.cityService = cityService;
    }

    @PostConstruct
    void postConstruct() {
        cityComboBox = new ComboBox<>();
        cityComboBox.setCellFactory(cellFactory(City::getName));
    }

}
