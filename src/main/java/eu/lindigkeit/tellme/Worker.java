package eu.lindigkeit.tellme;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Worker {

    private StringProperty clientName;
    public void setClientName(String value) {
        clientNameProperty().set(value);
    }
    public String getClientName() {
        return clientNameProperty().get();
    }
    public StringProperty clientNameProperty() {
        if (clientName == null) clientName = new SimpleStringProperty(this, "");
        return clientName;
    }

    private StringProperty date;
    public void setDate(String value) {
        dateProperty().set(value);
    }
    public String getDate() {
        return dateProperty().get();
    }
    public StringProperty dateProperty() {
        if (date == null) date = new SimpleStringProperty(this, "");
        return date;
    }

    private IntegerProperty timeSpent;
    public void setTimeSpent(Integer value) {
        timeSpentProperty().set(value);
    }
    public Integer getTimeSpent() {
        return timeSpentProperty().get();
    }
    public IntegerProperty timeSpentProperty() {
        if (timeSpent == null) timeSpent = new SimpleIntegerProperty(this, "");
        return timeSpent;
    }

    private StringProperty workType;
    public void setWorkType(String value) {
        workTypeProperty().set(value);
    }
    public String getWorkType() {
        return workTypeProperty().get();
    }
    public StringProperty workTypeProperty() {
        if (workType == null) workType = new SimpleStringProperty(this, "");
        return workType;
    }

    public StringProperty user;
    public void setUser(String value) {
        userProperty().set(value);
    }
    public String getUser() {
        return userProperty().get();
    }
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
