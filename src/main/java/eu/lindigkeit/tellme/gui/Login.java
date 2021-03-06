package eu.lindigkeit.tellme.gui;

import eu.lindigkeit.tellme.Database;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ResourceBundle;

/**
 * A login screen that offers login via email/password.
 * <p>
 *
 * @author Cedric Lindigkeit
 * @version 1.0
 * @since JDK 15
 */

public class Login extends Stage {

    private static final ResourceBundle LANG = ResourceBundle.getBundle("language");
    private static final Label INVALID_CREDENTIALS = new Label(LANG.getString("app.login.invalid"));

    public Login() {

        this.setTitle(LANG.getString("app.login"));
        this.setResizable(false);
        this.centerOnScreen();

        //Just the title

        Label loginL = new Label(LANG.getString("app.login"));
        loginL.setId("loginL");

        INVALID_CREDENTIALS.setId("invalid");
        INVALID_CREDENTIALS.setVisible(false);

        //Icon creation and styling

        FontIcon uIcon = new FontIcon(FluentUiFilledMZ.PERSON_MAIL_16);
        uIcon.setIconSize(24);
        uIcon.setIconColor(Color.web("#5DADE2"));

        FontIcon pIcon = new FontIcon(FluentUiFilledMZ.SHIELD_24);
        pIcon.setIconSize(24);
        pIcon.setIconColor(Color.web("#2874A6"));

        FontIcon subIcon = new FontIcon(FluentUiFilledAL.ARROW_RIGHT_24);
        subIcon.setIconSize(24);
        subIcon.setIconColor(Color.web("#34495E"));

        //Textfield creation and styling

        CustomTextField user = new CustomTextField();
        user.setLeft(uIcon);
        user.setId("userCTF");
        user.getStyleClass().add("form");

        CustomPasswordField password = new CustomPasswordField();
        password.setLeft(pIcon);
        password.setId("passwordCPF");
        password.getStyleClass().add("form");

        //Button creation and styling

        Button submit = new Button();
        submit.setGraphic(subIcon);
        submit.setId("submitB");
        submit.onMouseClickedProperty().set(event -> {

                    //Check if credentials are valid. If so, open the corresponding window. => Depending on their level of access.

                    if (submit(user, password)) {

                        if (Database.getLevel(user.getText()) == 0) new WorkerCenter(user.getText());
                        else new AdminCenter();
                        this.close();
                    } else INVALID_CREDENTIALS.setVisible(true);
                }
        );

        //Add everything to the scene

        Scene scene = new Scene(new Group(loginL, user, password, submit, INVALID_CREDENTIALS), 500, 300);
        scene.getStylesheets().add("login.css");
        this.setScene(scene);
        this.show();
    }

    /**
     * Checks if the credentials are valid.
     * <p>
     *
     * @param user     The username the user entered.
     * @param password The password the user entered.
     * @return true, if the credentials are valid.
     */

    public boolean submit(CustomTextField user, CustomPasswordField password) {

        String tmp = Database.checkUser(user.getText());
        boolean result = false;

        switch (tmp) {

            //Could have handled this better. Maybe I fix this later.

            case "CODE-10i" -> System.out.println("Establishing connection failed.");
            case "" -> System.out.println("User not found.");
            default -> {
                if (BCrypt.checkpw(password.getText(), tmp)) result = true;
                else System.out.println("Password incorrect.");
            }
        }
        return result;
    }
}
