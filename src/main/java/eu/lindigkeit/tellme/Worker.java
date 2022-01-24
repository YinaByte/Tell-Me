package eu.lindigkeit.tellme;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents a worker and the clients he has worked for.
 * One worker can have multiple clients.
 * Properties are used to bind the data to the GUI. I usually do not use them.
 * <p>
 *
 * @author Cedric Lindigkeit
 * @version 1.0
 * @since JDK 1.8
 */

public class Worker {

    private StringProperty clientName;

    public StringProperty clientNameProperty() {

        if (clientName == null) clientName = new SimpleStringProperty(this, "");
        return clientName;
    }

    private StringProperty date;

    public StringProperty dateProperty() {

        if (date == null) date = new SimpleStringProperty(this, "");
        return date;
    }

    private IntegerProperty timeSpent;

    public IntegerProperty timeSpentProperty() {

        if (timeSpent == null) timeSpent = new SimpleIntegerProperty(this, "");
        return timeSpent;
    }

    private StringProperty workType;

    public StringProperty workTypeProperty() {

        if (workType == null) workType = new SimpleStringProperty(this, "");
        return workType;
    }

    private StringProperty user;

    public StringProperty userProperty() {

        if (user == null) user = new SimpleStringProperty(this, "");
        return user;
    }

    public Worker(String user, LocalDate date) {

        this.userProperty().set(user);
        this.dateProperty().set(date.toString());
    }

    public Worker(String clientName, LocalDate date, int timeSpent, String workType) {

        this.clientNameProperty().set(clientName);
        this.dateProperty().set(date.toString());
        this.timeSpentProperty().set(timeSpent);
        this.workTypeProperty().set(workType);
    }

    public Worker(String user, String clientName, String date, int timeSpent, String workType) {

        this.userProperty().set(user);
        this.clientNameProperty().set(clientName);
        this.dateProperty().set(date);
        this.timeSpentProperty().set(timeSpent);
        this.workTypeProperty().set(workType);
    }

    //Simplified equals method

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Worker)) return false;
        return date.equals(((Worker) o).date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, date, timeSpent, workType, user);
    }
}
