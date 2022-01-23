package eu.lindigkeit.tellme;

import eu.lindigkeit.tellme.gui.Login;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        new Login();
    }

    public static void main(String[] args) {
        launch();
    }
}