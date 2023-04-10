package me.joshh.reportsystem.sql;

import me.joshh.reportsystem.ReportSystem;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    FileConfiguration config = ReportSystem.getPlugin(ReportSystem.class).getConfig();

    private String host = config.getString("sql.host");
    private String port = config.getString("sql.port");
    private String database = config.getString("sql.database");
    private String username = config.getString("sql.username");
    private String password = config.getString("sql.password");

    private Connection connection;


    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?useSSL=false",
                username, password);


    }


    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
