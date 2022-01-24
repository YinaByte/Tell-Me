package eu.lindigkeit.tellme.gui;

import eu.lindigkeit.tellme.Database;
import eu.lindigkeit.tellme.Worker;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * This class is the GUI for the Control center for the workers.
 * It also handles any occurring event
 * <p>
 *
 * @author Cedric Lindigkeit
 * @version 1.0.1
 * @since JDK 15
 */

@SuppressWarnings("unchecked")
public class WorkerCenter extends Stage {

    private static final ResourceBundle LANG = ResourceBundle.getBundle("language");

    public WorkerCenter(String user) {

        this.setTitle("Tell Me! - " + LANG.getString("workerhub"));
        this.setResizable(false);
        this.centerOnScreen();

        //Icon creation section

        FontIcon clIcon = new FontIcon(FluentUiFilledMZ.PERSON_16);
        clIcon.setIconSize(24);

        FontIcon tIcon = new FontIcon(FluentUiFilledAL.CLOCK_16);
        tIcon.setIconSize(24);

        //Creating all the components

        CustomTextField client = new CustomTextField();
        client.setPromptText(LANG.getString("workerhub.client"));
        client.setLeft(clIcon);
        client.setId("client");
        client.getStyleClass().add("form");

        TextArea workType = new TextArea();
        workType.setPromptText(LANG.getString("workerhub.worktype"));
        workType.setId("workType");
        workType.getStyleClass().add("form");

        CustomTextField timeSpent = new CustomTextField();
        timeSpent.setPromptText(LANG.getString("workerhub.time"));
        timeSpent.setLeft(tIcon);
        timeSpent.setId("time");
        timeSpent.getStyleClass().add("form");

        final LocalDate[] date = new LocalDate[1];

        DatePicker datePicker = new DatePicker();
        datePicker.setShowWeekNumbers(true);
        datePicker.setValue(LocalDate.now());
        datePicker.setId("date");
        datePicker.setOnAction(event -> date[0] = datePicker.getValue());

        ToggleGroup group = new ToggleGroup();

        ToggleButton clear = new ToggleButton(LANG.getString("workerhub.clear"));
        clear.setId("clear");
        clear.setToggleGroup(group);
        clear.onMouseClickedProperty().set(event -> {

                    client.clear();
                    workType.clear();
                    timeSpent.clear();
                    datePicker.setValue(LocalDate.now());
                }
        );

        ToggleButton submit = new ToggleButton(LANG.getString("workerhub.submit"));
        submit.setId("submit");
        submit.setToggleGroup(group);
        submit.onMouseClickedProperty().set(event -> {

                    //Could have given the user a response if one of the fields is empty. My bad.

                    if (client.getText().isEmpty() || workType.getText().isEmpty() || timeSpent.getText().isEmpty() || date[0] == null)
                        return;
                    Database.setEntry(user, client.getText(), date[0], Integer.parseInt(timeSpent.getText()), workType.getText());
                }
        );
        SegmentedButton button = new SegmentedButton();
        button.getButtons().addAll(clear, submit);
        button.setId("button");

        //Table creation section

        TableView2<Worker> table = new TableView2<>();
        ObservableList<Worker> info = Database.getWorkerInfo(user, "SELECT * FROM worker INNER JOIN work_details ON worker.worker_id = work_details.worker_id WHERE worker.username = '");

        TableColumn2<Worker, String> clientNameCol = new TableColumn2<>(LANG.getString("workerhub.client"));
        clientNameCol.setCellValueFactory(p -> p.getValue().clientNameProperty());
        TableColumn2<Worker, String> dateCol = new TableColumn2<>(LANG.getString("workerhub.date"));
        dateCol.setCellValueFactory(p -> p.getValue().dateProperty());
        TableColumn2<Worker, String> timeSpentCol = new TableColumn2<>(LANG.getString("workerhub.time"));
        timeSpentCol.setCellValueFactory(p -> p.getValue().timeSpentProperty().asString());
        TableColumn2<Worker, String> workTypeCol = new TableColumn2<>(LANG.getString("workerhub.worktype"));
        workTypeCol.setCellValueFactory(p -> p.getValue().workTypeProperty());

        table.getColumns().addAll(clientNameCol, dateCol, timeSpentCol, workTypeCol);
        table.setItems(info);
        table.setRowHeaderVisible(true);
        table.setId("table");
        table.setEditable(false);

        //Adding all the components to the scene

        Scene scene = new Scene(new Group(client, workType, datePicker, table, timeSpent, button), 600, 400);
        scene.getStylesheets().add("workerhub.css");

        this.setScene(scene);
        this.show();

        //The button size depends on the used language. Its position is set here to make sure it is always centered on
        // the x-axis. This has to be done after the creation of the stage.

        button.setTranslateX((600 - button.getWidth()) / 2);
    }
}
