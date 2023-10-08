package application.in.service;

import domain.model.Player;
import domain.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


class AuthorizationTest {

    private Authorization authorization;

    private PlayerRepository playerRepository = new PlayerRepository();

    private Player player;

    @BeforeEach
    void beforeAllTests() {
        player = new Player("artur", "artur@mail.ru", "artur");
        playerRepository.savePlayer(player.getName(), player.getEmail(), player.getPassword());
        authorization = new Authorization(playerRepository);
    }

    @Test
    void authorize() {
        String input = "artur@mail.ru\nartur\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        authorization.authorize();
        Assertions.assertEquals(1, playerRepository.getPlayer("artur@mail.ru").getAudit().size());
    }
}