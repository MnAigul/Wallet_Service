package domain.dao;

import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.DBUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


class TransactionDAOTest{

    private TransactionDAO transactionDAO;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.3-alpine"
    );


    @BeforeAll
    static void beforeAll() {
        postgres.start();
        DBUtils connectionProvider = new DBUtils(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        DBUtils.executeLiquiBase();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() throws SQLException, IOException {
        transactionDAO = new TransactionDAO();

    }



    @Test
    void saveTransaction() throws IOException {
        Transaction transaction2 = new Transaction(1L, 1L, TransactionType.CREDIT, BigDecimal.valueOf(200));
        boolean f = transactionDAO.saveTransaction(transaction2);
        Assert.assertEquals(true, f);
        Transaction transaction_notExec = new Transaction(1L, 1L, TransactionType.CREDIT, BigDecimal.valueOf(100));
        boolean f2 = transactionDAO.saveTransaction(transaction_notExec);
        Assert.assertEquals(false, f2);

    }

    @Test
    void getAllOperationsOfPlayer() throws IOException, SQLException {
        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.savePlayer("user", "user", "user");
        Transaction transaction2 = new Transaction(2L, 2L, TransactionType.CREDIT, BigDecimal.valueOf(200));
        transactionDAO.saveTransaction(transaction2);
        Transaction transaction3 = new Transaction(3L, 2L, TransactionType.CREDIT, BigDecimal.valueOf(200));
        transactionDAO.saveTransaction(transaction3);
        List<Transaction> transactions = transactionDAO.getAllOperationsOfPlayer(2L);
        Assert.assertEquals(2, transactions.size());

    }
}