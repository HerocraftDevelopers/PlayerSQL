package com.mengcraft.playerSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

public class Database {
    public static Connection connection;

    private static String[] getSQLConfig() {
        Plugin plugin = PlayerSQL.plugin;
        return new String[]{plugin.getConfig().getString("mysql.addr"), plugin.getConfig().getString("mysql.port"),
                plugin.getConfig().getString("mysql.data"), plugin.getConfig().getString("mysql.user"),
                plugin.getConfig().getString("mysql.pass")};
    }

    public static Boolean getConnect() {
        if (connection != null) try {
            if (connection.isClosed()) {
                connection = null;
                return false;
            } else return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        else return false;
        return false;
    }

    public static Boolean closeConnect() {
        if (getConnect()) try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Boolean openConnect() {
        if (getConnect()) {
            return true;
        } else {
            String[] sqlConfig = getSQLConfig();
            String addr = sqlConfig[0];
            String port = sqlConfig[1];
            String data = sqlConfig[2];
            String user = sqlConfig[3];
            String pass = sqlConfig[4];
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + addr + ":" + port + "/" + data, user, pass);
                return true;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean createTables() {
        if (openConnect()) try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + "PlayerSQL (" + "Id int NOT NULL AUTO_INCREMENT, "
                    + "PlayerName text, " + "Locked int, " + "Health int, " + "Food int, " + "Level int, "
                    + "Armor text, " + "Economy double, " + "Inventory text, " + "EndChest text, " + "PRIMARY KEY (Id));";
            statement.executeUpdate(sql);
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
