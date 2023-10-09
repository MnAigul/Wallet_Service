package domain.repository;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private TransactionRepository transactionRepository = new TransactionRepository();

    @BeforeEach
    void beforeEachTest() {
        Transaction transaction1 = new Transaction(1L, TransactionType.DEBIT, BigDecimal.valueOf(400));
        Transaction transaction2 = new Transaction(2L, TransactionType.DEBIT, BigDecimal.valueOf(400));
        transactionRepository.transactions.put(transaction1.getId(), transaction1);
        transactionRepository.transactions.put(transaction2.getId(), transaction2);

    }

    @Test
    void transactionWithIdExists1() {
        Assert.assertTrue(transactionRepository.transactionWithIdExists(1L));
    }

    @Test
    void transactionWithIdExists2() {
        Assert.assertFalse(transactionRepository.transactionWithIdExists(3L));
    }

    @Test
    void saveTransaction() {
        Transaction new_transaction = new Transaction(3L,TransactionType.DEBIT, BigDecimal.valueOf(200));
        transactionRepository.saveTransaction(new_transaction.getId(), new_transaction.getType(), new_transaction.getSum());
        Assert.assertEquals(3, transactionRepository.transactions.size());

    }

    @Test
    void saveTransaction2() {
        Transaction new_transaction = new Transaction(1L,TransactionType.DEBIT, BigDecimal.valueOf(200));
        transactionRepository.saveTransaction(new_transaction.getId(), new_transaction.getType(), new_transaction.getSum());
        Assert.assertEquals(2, transactionRepository.transactions.size());

    }
}