module eu.lindigkeit.tellme {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires spring.security.crypto;
    requires org.mariadb.jdbc;

    opens eu.lindigkeit.tellme to javafx.fxml;
    exports eu.lindigkeit.tellme;
}