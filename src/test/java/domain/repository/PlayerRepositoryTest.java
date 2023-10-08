package domain.repository;

import domain.model.Player;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlayerRepositoryTest {

    private PlayerRepository playerRepository = new PlayerRepository();
    private Player player1, player2, player3;

    @BeforeEach
    void beforeEachTest() {
        player1 = new Player("artur", "artur@mail.ru", "artur");
        player2 = new Player("s", "s@mail.ru", "s");
        player3 = new Player("r", "r@mail.ru", "r");
        playerRepository.players.put(player1.getEmail(), player1);
        playerRepository.players.put(player2.getEmail(), player2);
        playerRepository.players.put(player3.getEmail(), player3);
    }

    @Test
    void emailExists1() {
        Assert.assertTrue(playerRepository.emailExists("artur@mail.ru"));
    }

    @Test
    void emailExists2() {
        Assert.assertFalse(playerRepository.emailExists("ivan@mail.ru"));
    }

    @Test
    void playerExists1() {
        Player player = playerRepository.playerExists(player1.getEmail(), player1.getPassword());
        Assert.assertEquals(player1, player);
    }

    @Test
    void playerExists2() {
        Player player = playerRepository.playerExists("ivan", "ivan");
        Assert.assertEquals(null, player);
    }

    @Test
    void savePlayer1() {
        Player expected_player = new Player("vova", "vova", "vova");
        Player actual_player = playerRepository.savePlayer(expected_player.getName(), expected_player.getEmail(), expected_player.getPassword());
        Assert.assertEquals(expected_player, actual_player);
        Assert.assertEquals(4, playerRepository.players.size());
    }

    @Test
    void savePlayer2() {
        Player expected_player = null;
        Player actual_player = playerRepository.savePlayer(player1.getName(), player1.getEmail(), player1.getPassword());
        Assert.assertEquals(expected_player, actual_player);
        Assert.assertEquals(3, playerRepository.players.size());
    }

    @Test
    void updateBalance() {
        playerRepository.updateBalance(player1, BigDecimal.valueOf(500));
        Assert.assertEquals(BigDecimal.valueOf(500), playerRepository.getPlayer(player1.getEmail()).getMoney());
    }

    @Test
    void getPlayer1() {
        Player expected_player = null;
        Player actual_player = playerRepository.getPlayer("notExists@mail.ru");
        assertEquals(expected_player, actual_player);
    }

    @Test
    void getPlayer2() {
        Player expected_player = player1;
        Player actual_player = playerRepository.getPlayer(player1.getEmail());
        assertEquals(expected_player, actual_player);
    }

}