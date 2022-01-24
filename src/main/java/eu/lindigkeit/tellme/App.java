package eu.lindigkeit.tellme;

import eu.lindigkeit.tellme.gui.Login;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class of the application.
 * <p>
 *
 * @author Cedric Lindigkeit
 * @version 1.0
 * @since JDK 1.8
 */

public class App extends Application {

    @Override
    public void start(Stage stage) {

        new Login();
    }

    public static void main(String[] args) {
        launch();
    }
}