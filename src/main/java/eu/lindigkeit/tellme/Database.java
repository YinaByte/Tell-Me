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

public abstract class Database {

    private Database() {
    }

    protected static final String[] CONN_S = new String[]{"jdbc:mariadb://mysql02.manitu.net:3306/db75746", "u75746", "MV76Ee5sm6e4TgbE"};

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
            return "CODE-10i";
        }
        return "";
    }

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
            return 2;
        }
        return 2;
    }

    public static ObservableList<Worker> getWorkerInfo(String user) {

        ObservableList<Worker> worker = FXCollections.observableArrayList();

        ResultSet rs;
        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT * FROM worker INNER JOIN client ON worker.worker_id = client.worker_id INNER JOIN work_details ON worker.worker_id = work_details.worker_id WHERE worker.username = '" + user + "'");
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

    public static ObservableList<Worker> getAdminInfo(String user) {

        ObservableList<Worker> worker = FXCollections.observableArrayList();

        try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
            ResultSet rs;
            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT * FROM worker INNER JOIN work_details ON worker.worker_id = work_details.worker_id WHERE username = '" + user + "'");
                rs = stmt.getResultSet();
            }

            while (rs.next()) worker.add(new Worker(user, rs.getString("client_name"), rs.getDate("date").toLocalDate().toString(), rs.getInt("time_spent"), rs.getString("work_type")));
            return worker;
        } catch (SQLException e) {

            e.printStackTrace();
            return FXCollections.emptyObservableList();
        }
    }

    public static boolean setEntry(String user, String clientName, LocalDate date, int time, String workType) {

        try {
            ResultSet rs;
            try (Connection conn = getConnection(CONN_S[0], CONN_S[1], CONN_S[2])) {
                try (Statement stmt = conn.createStatement()) {

                    stmt.execute("INSERT INTO work_details (worker_id, client_name, date, time_spent, work_type) VALUES ((SELECT worker_id FROM worker WHERE username = '" + user + "'), '" + clientName + "', '" + date + "', " + time + ", '" + workType + "')");
                    rs = stmt.getResultSet();
                }
            }

            return rs.next();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return false;
    }

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
