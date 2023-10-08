package domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerTest {

    private Player player;

    @BeforeAll
    void beforeAllTests() {
        player = new Player("a", "a", "a");
    }


    @Test
    void addNewTransactionToPlayer() {
        Transaction transaction1 = new Transaction(1L, TransactionType.DEBIT, BigDecimal.valueOf(500));
        Transaction transaction2 = new Transaction(1L, TransactionType.CREDIT, BigDecimal.valueOf(200));
        Transaction transaction3 = new Transaction(12L, TransactionType.DEBIT, BigDecimal.valueOf(0));
        player.addNewTransactionToPlayer(transaction1);
        player.addNewTransactionToPlayer(transaction2);
        player.addNewTransactionToPlayer(transaction3);
        Assertions.assertEquals(3, player.getPlayerTransactions().size());
    }

    @Test
    void addNewActionToAudit() {
        Audit audit1 = new Audit(ActionType.DEBIT);
        Audit audit2 = new Audit(ActionType.CREDIT);
        player.addNewActionToAudit(audit1);
        player.addNewActionToAudit(audit2);
        Assertions.assertEquals(2, player.getAudit().size());
    }
}