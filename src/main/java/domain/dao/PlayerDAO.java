package domain.dao;


import domain.model.Player;
import domain.model.Role;
import infrastructure.DBUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Класс доступа к базе данных для объектов типа Player {@link Player}
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class PlayerDAO {

    /**
     * Функция нахождения игрока по его email
     * @param email - имэйл Игрока
     * @return Player, если такой игрок сущ-ет, в противном случае возвращается null
     */
    public Player findByEmail(String email) throws SQLException {
        Connection connection = null;
        Player player = null;
        try {
            connection = DBUtils.getConnection();
            String existsEmailSQL = "SELECT * FROM my_schema.players WHERE email = '" + email + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(existsEmailSQL);
            if (resultSet.next()) {
                player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setName(resultSet.getString("name"));
                player.setEmail(resultSet.getString("email"));
                player.setPassword(resultSet.getString("password"));
                player.setMoney(resultSet.getBigDecimal("money"));
                player.setRole(Role.valueOf(resultSet.getString("role")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }

        return player;
    }

    /**
     * Функция нахождения игрока по его email и password
     * @param email - имэйл Игрока
     * @param password - пароль Игрока
     * @return Player, если такой игрок сущ-ет, в противном случае возвращается null
     */
    public Player findByEmailAndPassword(String email, String password) throws SQLException, IOException {
        Connection connection = null;
        Player player = null;
        try {
            connection = DBUtils.getConnection();
            String existsEmailSQL = "SELECT * FROM my_schema.players WHERE email = '" + email + "' and password = '" + password + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(existsEmailSQL);
            if (resultSet.next()) {
                player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setName(resultSet.getString("name"));
                player.setEmail(resultSet.getString("email"));
                player.setPassword(resultSet.getString("password"));
                player.setMoney(resultSet.getBigDecimal("money"));

                player.setRole(Role.valueOf(resultSet.getString("role")));
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
        return player;
    }

    /**
     * Функция сохранения игрока в таблице players
     * @param name имя игрока
     * @param email имэйл игрока
     * @param password пароль игрока
     * @return Player, если такой игрок сущ-ет в БД, в противном случае возвращается null
     */
    public Player savePlayer(String name, String email, String password) throws SQLException, IOException {
        Connection connection = null;
        Player result = null;
        try {
            connection = DBUtils.getConnection();
            if (findByEmail(email) == null) {
                String insertDataSQL = "INSERT INTO my_schema.players (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
                insertDataStatement.setString(1, name);
                insertDataStatement.setString(2, email);
                insertDataStatement.setString(3, password);
                insertDataStatement.executeUpdate();
                result = findByEmailAndPassword(email, password);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
       return result;
    }

    /**
     * Функция обновления баланса игрока в таблице players
     * @param player {@link Player}
     * @param sum новый баланс игрока
     */
    public void updateBalance(Player player, BigDecimal sum) throws SQLException, IOException {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            player.setMoney(sum);
            String updateDataSQL =  "UPDATE my_schema.players SET money = ? WHERE id = ?";
            PreparedStatement updateDataStatement = connection.prepareStatement(updateDataSQL);
            updateDataStatement.setBigDecimal(1, sum);
            updateDataStatement.setLong(2, player.getId());
            updateDataStatement.executeUpdate();
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
