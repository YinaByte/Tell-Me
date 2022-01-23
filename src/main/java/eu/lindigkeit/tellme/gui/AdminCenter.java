package eu.lindigkeit.tellme.gui;

import eu.lindigkeit.tellme.Database;
import eu.lindigkeit.tellme.Worker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ResourceBundle;

public class AdminCenter extends Stage {

    private static final ResourceBundle LANG = ResourceBundle.getBundle("language");

    public AdminCenter() {

        this.setTitle("Tell Me! - " + LANG.getString("adminhub"));
        this.setResizable(false);
        this.centerOnScreen();

        SearchableComboBox<String> comboBox = new SearchableComboBox<>();
        comboBox.getItems().addAll(Database.getAllWorkerNames());
        comboBox.setId("comboBox");

        TableView2<Worker> tableView = new TableView2<>();
        tableView.setId("tableView");
        tableView.setRowHeaderVisible(true);

        TableColumn2<Worker, String> clientNameCol = new TableColumn2<>(LANG.getString("workerhub.client"));
        clientNameCol.setCellValueFactory(p -> p.getValue().clientNameProperty());
        clientNameCol.setPrefWidth(120);
        TableColumn2<Worker, String> dateCol = new TableColumn2<>(LANG.getString("workerhub.date"));
        dateCol.setCellValueFactory(p -> p.getValue().dateProperty());
        dateCol.setPrefWidth(120);
        TableColumn2<Worker, String> timeSpentCol = new TableColumn2<>(LANG.getString("workerhub.time"));
        timeSpentCol.setCellValueFactory(p -> p.getValue().timeSpentProperty().asString());
        timeSpentCol.setPrefWidth(120);
        TableColumn2<Worker, String> workTypeCol = new TableColumn2<>(LANG.getString("workerhub.worktype"));
        workTypeCol.setCellValueFactory(p -> p.getValue().workTypeProperty());
        workTypeCol.setPrefWidth(200);

        tableView.getColumns().addAll(clientNameCol, dateCol, timeSpentCol, workTypeCol);
        tableView.setId("tableView");

        FontIcon bIcon = new FontIcon(FluentUiFilledMZ.SEARCH_INFO_24);
        bIcon.setIconSize(24);
        bIcon.setIconColor(Color.web("#34495E"));

        Button submit = new Button();
        submit.setGraphic(bIcon);
        submit.setId("submit");
        submit.onMouseClickedProperty().set(event -> tableView.setItems(Database.getAdminInfo(comboBox.getValue()))
        );

        Scene scene = new Scene(new Group(tableView, comboBox, submit), 600, 400);
        scene.getStylesheets().add("adminhub.css");

        this.setScene(scene);
        this.show();
    }
}
