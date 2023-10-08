package application.in.service;

import domain.model.Player;
import domain.model.TransactionType;
import domain.repository.PlayerRepository;
import domain.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private PlayerRepository playerRepository = new PlayerRepository();

    @Mock
    private TransactionRepository transactionRepository;



    private Player player;
    @BeforeEach
    void beforeAllTests() {
        player = new Player("a", "a", "a");
        player.setMoney(BigDecimal.valueOf(500L));
        playerRepository.savePlayer(player.getName(), player.getEmail(), player.getPassword());
        transactionService = new TransactionServiceImpl(transactionRepository, playerRepository, player);
    }

    @Test
    void debit() {
        Mockito.when(transactionRepository.transactionWithIdExists(1L)).thenReturn(false);
        transactionService.debit(1L, BigDecimal.valueOf(200));
        Assertions.assertEquals(BigDecimal.valueOf(300), player.getMoney());
        transactionService.debit(1L, BigDecimal.valueOf(0));
        Assertions.assertEquals(BigDecimal.valueOf(300), player.getMoney());
    }

    @Test
    void credit() {
        Mockito.when(transactionRepository.transactionWithIdExists(1L)).thenReturn(false);
        transactionService.credit(1L, BigDecimal.valueOf(1000));
        Assertions.assertEquals(BigDecimal.valueOf(1500), player.getMoney());
        transactionService.credit(1L, BigDecimal.valueOf(0));
        Assertions.assertEquals(BigDecimal.valueOf(1500), player.getMoney());
    }

}