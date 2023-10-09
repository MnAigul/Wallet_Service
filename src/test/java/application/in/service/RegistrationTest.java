package application.in.service;

import domain.model.Player;
import domain.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


class RegistrationTest {
    private Registration registration;

    private PlayerRepository playerRepository = new PlayerRepository();

    private Player player;

    @BeforeEach
    void beforeAllTests() {
        player = new Player("r", "r@mail.ru", "r");
        playerRepository.savePlayer(player.getName(), player.getEmail(), player.getPassword());
        registration = new Registration(playerRepository);
    }


    @Test
    void registryWhenEmailNotExists() {
        String input = "aigul\naigul@mail.ru\naigul\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        registration.registry();
        Assertions.assertEquals(1, playerRepository.getPlayer("aigul@mail.ru").getAudit().size());
    }

}