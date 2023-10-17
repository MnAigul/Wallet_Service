package domain.dao;

import domain.model.Player;
import domain.model.Role;
import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.DBUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TransactionDAO {



    private Connection connection = null;

    public boolean saveTransaction(Transaction transaction) throws IOException {
        boolean res = false;
        try {
            connection = DBUtils.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String insert1 = "INSERT INTO my_schema.transactions (id, player_id, type, sum) VALUES (" + transaction.getId() + ", " + transaction.getPlayerId() + ", '" + transaction.getType().toString() + "', " + transaction.getSum() + ")";
            statement.executeUpdate(insert1);
            connection.commit();
            System.out.println("Транзакция успешно завершена.");
            res = true;
        } catch (SQLException e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена, так как транзакция с таким id уже существует");
                } catch (SQLException rollbackException) {
                    System.err.println("Ошибка при откате транзакции: " + rollbackException.getMessage());
                }
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
        return res;
    }

    public List<Transaction> getAllOperationsOfPlayer(Long player_id) {
        List<Transaction> transactions = new LinkedList<>();
        try {
            connection = DBUtils.getConnection();
            String existsEmailSQL = "SELECT * FROM my_schema.transactions WHERE player_id = " + player_id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(existsEmailSQL);
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getLong("id"));
                transaction.setPlayerId(resultSet.getLong("player_id"));
                transaction.setType(TransactionType.valueOf(resultSet.getString("type")));
                transaction.setSum(resultSet.getBigDecimal("sum"));
                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
        return transactions;
    }


}
