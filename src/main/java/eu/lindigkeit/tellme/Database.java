package eu.lindigkeit.tellme;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;

import static java.sql.DriverManager.getConnection;

/**
 * Implements the database connection and the SQL queries.
 * The database's type is MariaDB.
 * <p>
 *
 * @author Cedric Lindigkeit
 * @version 1.0
 */

public abstract class Database {

    //To be honest: SonarLint just wanted me to create a private constructor to "hide the implicit one". I have no clue
    // what it does.

    private Database() {}

    protected static final String[] CONN_S = new String[]{"jdbc:mariadb://mysql02.manitu.net:3306/db75746", "u75746", "MV76Ee5sm6e4TgbE"};

    /**
     * This method is used to check if a user exists in the database.
     * If so, it returns his password as a hash
     * <p>
     *
     * @param user The username to check
     * @return The password hash as a String. If the user does not exist, it returns an empty String.
     */

    public static String checkUser(String user) {

        ResultSet rs;
        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT password FROM worker WHERE username = '" + user + "'");
                rs = stmt.getResultSet();
            }

            if (rs.next()) return rs.getString("password");
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return "";
    }

    /**
     * This method is used to get the level of a user.
     * <p>
     *
     * @param user The user to get the level of
     * @return The level of the user as an int. The returned levels are:
     * <ul>
     *     <li>0: Worker</li>
     *     <li>1: Administrator</li>
     *     <li>2: User not found</li>
     *     <li>3: Database error</li>
     * </ul>
     */

    public static byte getLevel(String user) {

        ResultSet rs;
        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT level FROM worker WHERE username = '" + user + "'");
                rs = stmt.getResultSet();
            }

            if (rs.next()) return (byte) rs.getInt("level");
        } catch (SQLException e) {

            e.printStackTrace();
            return 3;
        }
        return 2;
    }

    /**
     * This method fetches all the data from the database.
     * This data consists of the following:
     * <ul>
     *     <li>The clients name</li>
     *     <li>The date worked</li>
     *     <li>The time worked</li>
     *     <li>Description of the work</li>
     * </ul>
     * The name of the worker is not returned because it is the worker who is logged in.
     * <p>
     *
     * @param user The user to get the data of
     * @return An ObservableList of the data. If there is an error, it returns an empty ObservableList.
     */

    public static ObservableList<Worker> getWorkerInfo(String user, String sqlStatement) {

        ObservableList<Worker> worker = FXCollections.observableArrayList();

        ResultSet rs;
        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            try (Statement stmt = conn.createStatement()) {

                stmt.execute(sqlStatement + user + "");
                rs = stmt.getResultSet();
            }

            if (rs.next()) {

                worker.add(new Worker(rs.getString("client_name"), rs.getDate("date").toLocalDate(), rs.getInt("time_spent"), rs.getString("work_type")));
            }
            return worker;
        } catch (SQLException e) {

            e.printStackTrace();
            return FXCollections.emptyObservableList();
        }
    }

    /**
     * This method is user to set an entry in the database.
     * <p>
     *     The entry consists of the following:
     *     <ul>
     *         <li>The username</li>
     *         <li>The client name</li>
     *         <li>The date worked</li>
     *         <li>The time worked</li>
     *         <li>The description of the work done</li>
     *     </ul>
     * </p>
     * @param user The user to set the entry for
     * @param clientName The name of the client
     * @param date The date worked
     * @param time The time worked
     * @param workType The description of the work done
     */

    public static void setEntry(String user, String clientName, LocalDate date, int time, String workType) {

        try {
            ResultSet rs;
            try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
                try (Statement stmt = conn.createStatement()) {

                    stmt.execute("INSERT INTO work_details (worker_id, client_name, date, time_spent, work_type) VALUES ((SELECT worker_id FROM worker WHERE username = '" + user + "'), '" + clientName + "', '" + date + "', " + time + ", '" + workType + "')");
                    rs = stmt.getResultSet();
                }
            }

            rs.next();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * This method is used to get the names of all workers.
     * It does not return the names of administrators.
     * <p>
     *
     * @return A String array of all the names of the workers.
     */

    public static String[] getAllWorkerNames() {

        String[] names = new String[0];

        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            ResultSet rs;
            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT username FROM worker WHERE NOT level = 1");
                rs = stmt.getResultSet();
            }

            while (rs.next()) {

                names = Arrays.copyOf(names, names.length + 1);
                names[names.length - 1] = rs.getString("username");
            }
            return names;
        } catch (SQLException e) {

            e.printStackTrace();
            return names;
        }
    }
}
