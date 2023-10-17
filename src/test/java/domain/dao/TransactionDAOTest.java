package domain.dao;

import config.ContainersEnvironment;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

class TransactionDAOTest extends ContainersEnvironment {

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();


    private TransactionDAO transactionDAO = new TransactionDAO();

    @Test
    void saveTransaction() throws IOException {
        Transaction transaction = new Transaction(1L, 2L, TransactionType.DEBIT, BigDecimal.valueOf(500));
        transactionDAO.saveTransaction(transaction);

    }

    @Test
    void getAllOperationsOfPlayer() {
    }
}