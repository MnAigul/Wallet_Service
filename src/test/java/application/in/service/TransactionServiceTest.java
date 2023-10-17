package application.in.service;

import domain.dao.PlayerDAO;
import domain.dao.TransactionDAO;
import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.DBUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

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
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }


    TransactionService transactionService = new TransactionService();
    TransactionDAO transactionDAO = new TransactionDAO();

    @Test
    void debitAndCredit1() throws IOException, SQLException {
        Player player = (new PlayerDAO()).findByEmail("admin");
        Transaction transaction = new Transaction(1L, 1L, TransactionType.DEBIT, BigDecimal.valueOf(500));
        transactionService.debitAndCredit(transaction, player);
        Assert.assertEquals("Недостаточно средств!\n", output.toString());
    }

    @Test
    void debitAndCredit2() throws IOException, SQLException {
        Player player = (new PlayerDAO()).findByEmail("admin");
        Transaction transaction = new Transaction(2L, 1L, TransactionType.CREDIT, BigDecimal.valueOf(500));
        transactionService.debitAndCredit(transaction, player);
        Assert.assertEquals("Транзакция успешно завершена.\nОперация кредита прошла успешно!\n", output.toString());
    }

    @Test
    void allOperationsOfPlayer() throws IOException, SQLException {
        Player player = (new PlayerDAO()).savePlayer("dima", "dima", "dima");
        transactionDAO.saveTransaction(new Transaction(45L, player.getId(), TransactionType.CREDIT, BigDecimal.valueOf(500)));
        transactionDAO.saveTransaction(new Transaction(46L, player.getId(), TransactionType.DEBIT, BigDecimal.valueOf(300)));
        transactionService.allOperationsOfPlayer(player);
        Assert.assertEquals("Транзакция успешно завершена.\n" +
                "Транзакция успешно завершена.\n" + TransactionType.CREDIT + " на сумму: " + BigDecimal.valueOf(500) + "\n" + TransactionType.DEBIT + " на сумму: " + BigDecimal.valueOf(300) + "\n", output.toString());

    }
}