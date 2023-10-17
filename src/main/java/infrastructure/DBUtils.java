package infrastructure;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для подсоединения к БД
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class DBUtils {

    /** Поле для хранения URL базы данных. */
    private static String URL = null;

    /** Поле для хранения имени пользователя базы данных. */
    private static String USER_NAME = "";

    /** Поле для хранения пароля пользователя базы данных. */
    private static String PASSWORD = "";

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param jdbcUrl - URL базы данных
     * @param username - имя пользователя
     * @param password - пароль пользователя
     */
    public DBUtils(String jdbcUrl, String username, String password) {
        URL = jdbcUrl;
        USER_NAME = username;
        PASSWORD = password;
    }


    /**
     * Получает соединение с базой данных.
     * @return объект Connection для работы с базой данных
     * @throws IOException если не удается загрузить настройки подключения к базе данных
     */
    public static Connection getConnection() throws IOException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Got SQL Exception " + e.getMessage());
        }
        return connection;
    }

    /**
     * Выполняет миграции базы данных с помощью Liquibase.
     * @throws SQLException если возникает ошибка при работе с базой данных
     * @throws IOException если не удается загрузить настройки подключения к базе данных
     */
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

    /**
     * Меняет URL базы данных перед выполнением миграций с помощью Liquibase.
     * Создает схему liquibase, если ее нет.
     * @throws SQLException если возникает ошибка при работе с базой данных
     * @throws IOException  если не удается загрузить настройки подключения к базе данных
     */
    public static void changeURLBeforeLiquiBase() throws SQLException, IOException {
        Connection connection = null;
        try {
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