package infrastructure;


import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@RequiredArgsConstructor
public class DBUtils {

    private static String URL = null;
    private static String USER_NAME = "";
    private static String PASSWORD = "";



    public static Connection getConnection() throws IOException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Got SQL Exception " + e.getMessage());
        }
        return connection;
    }


    public static void executeLiquiBase() {
        Connection connection = null;
        try {
            DBUtils.changeURLBeforeLiquiBase();
            connection = getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
    }

    public static void changeURLBeforeLiquiBase() throws SQLException, IOException {
        FileInputStream fis;
        Properties properties = new Properties();
        Connection connection = null;
        try {
            fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            URL = properties.getProperty("url");
            USER_NAME = properties.getProperty("username");
            PASSWORD = properties.getProperty("password");
            connection = getConnection();
            Statement statement = connection.createStatement();
            String sqlScript = "create schema liquibase";
            statement.executeUpdate(sqlScript);
            URL = URL + "?currentSchema=liquibase";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
    }
}