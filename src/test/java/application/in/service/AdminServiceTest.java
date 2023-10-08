package application.in.service;

import domain.model.ActionType;
import domain.model.Audit;
import domain.model.Player;
import domain.repository.PlayerRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminServiceTest {

    private PlayerRepository playerRepository = new PlayerRepository();
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private AdminService adminService = new AdminService(playerRepository);
    private Player player1, player2, player3;

    @BeforeEach
    void beforeEachTest() {
        player1 = new Player("artur", "artur@mail.ru", "artur");
        player2 = new Player("s", "s@mail.ru", "s");
        player3 = new Player("r", "r@mail.ru", "r");
        playerRepository.savePlayer(player1.getName(), player1.getEmail(), player1.getPassword());
        playerRepository.savePlayer(player2.getName(), player2.getEmail(), player2.getPassword());
        playerRepository.savePlayer(player3.getName(), player3.getEmail(), player3.getPassword());
    }

    @BeforeEach
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @Test
    void authorizeAdmin1() {
        String input = "0";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        adminService.authorizeAdmin("admin", "admin");
        Assert.assertEquals("--Вы вошли как админ--\n" +
                "--Выбери дальнейшие действия:--\n" +
                "Нажми '0' для выхода из личного кабинета.\n" +
                "Нажми '1' для просмотра email-ов всех зарегистрированных игроков.\n" +
                "Нажми '2' для аудита дйствий игрока.\n", output.toString());
    }

    @Test
    void authorizeAdmin2() {
        adminService.authorizeAdmin("user", "user");
        Assert.assertEquals("Неверный email или пароль\n", output.toString());
    }


    @Test
    void getAllActionsOfPlayer1() {
        adminService.getAllActionsOfPlayer("usernNotExists");
        Assert.assertEquals("There is no player with this email!\n", output.toString());
    }

    @Test
    void getAllActionsOfPlayer2() {
        player1.addNewActionToAudit(new Audit(ActionType.AUTHORISATION));
        player1.addNewActionToAudit(new Audit(ActionType.EXIT));
        adminService.getAllActionsOfPlayer(player1.getEmail());
        Assert.assertEquals(2, player1.getAudit().size());
    }
}