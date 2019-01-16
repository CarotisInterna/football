package ru.vsu.football.ui.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import ru.vsu.football.domain.City;
import ru.vsu.football.service.impl.CityService;
import ru.vsu.football.ui.TableUIComponent;
import ru.vsu.football.ui.ViewResolver;

import java.util.List;
import java.util.Optional;

@Component("Города")
public class CitiesComponent extends TableUIComponent<City> {
    private CityService cityService;
    private static final String WRONG_FIELDS = "Название от 1 до 30 символов, содержит только русские буквы";
    private ObservableList<City> cities;

    @Override
    public void fillTable() {
        super.fillTable();
        cities = FXCollections.observableList(cityService.fetch());
        table.setItems(cities);
    }

    @Autowired
    public CitiesComponent(ViewResolver viewResolver, CityService cityService) {
        super(viewResolver);
        this.cityService = cityService;
    }

    @Override
    protected void buildTableInternal() {
        final TableColumn<City, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        final TableColumn<City, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        table.getColumns().addAll(idColumn, nameColumn);

        nameColumn.setOnEditCommit(e -> {
            City city = (City) ((TableColumn.CellEditEvent) e).getRowValue();
            String newName = (String) ((TableColumn.CellEditEvent) e).getNewValue();
            if (newName.length() > 255) {
                viewResolver.showError(WRONG_FIELDS);
            } else {
                city.setName(newName);
                cityService.save(city);
            }
            table.refresh();
        });
    }

    @Override
    protected VBox addPane(City domain) {
        VBox newCityPane = new VBox();
        final TextField nameField = domain == null ? new TextField() : new TextField(domain.getName());
        nameField.setPromptText("Название");
        final Button saveButton = new Button("Сохранить");
        newCityPane.getChildren().addAll(nameField, saveButton);
        saveButton.setOnAction(event -> {
            try {
                String fieldText = nameField.getText().trim();
                String name = fieldText.substring(0, 1).toUpperCase() + fieldText.substring(1);
                if (name.length() < 1 || name.length() > 30 || !name.matches("[а-яА-Я\\-]+")) {
                    viewResolver.showError(WRONG_FIELDS);
                } else {

                    City newCity = Optional.ofNullable(domain).orElse(new City());
                    newCity.setName(name);
                    newCity = cityService.save(newCity);
                    updateView();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewResolver.showError(WRONG_FIELDS);
            } catch (JpaSystemException ex) {
                viewResolver.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            }
        });
        return newCityPane;
    }

    @Override
    protected List<City> fetch() {
        return cityService.fetch();
    }

    @Override
    protected void deleteElement(City domain) {
        cityService.delete(domain);
    }
}
