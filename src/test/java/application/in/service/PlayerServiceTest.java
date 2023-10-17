package application.in.service;

import domain.dao.AuditDAO;
import domain.dao.PlayerDAO;
import domain.model.AuditType;
import domain.model.Player;
import infrastructure.DBUtils;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PlayerDAO playerDAO;
    private PlayerService playerService;

    @BeforeEach
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

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
        playerDAO = new PlayerDAO();
        playerService = new PlayerService();

    }

    @Test
    void getBalance() throws SQLException, IOException {
        Player player = playerDAO.savePlayer("user", "user", "user");
        Assert.assertEquals(BigDecimal.valueOf(0), playerService.getBalance(player));
    }

    @Test
    void registry() throws SQLException, IOException {
        String input = "aigul\naigul@mail.ru\naigul\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        playerService.registry(scanner);
        Assert.assertEquals("Введите своё имя:\n" +
                "Введите свой email:\n" +
                "Введите свой пароль:\nРегистрация прошла успешно!\n", output.toString());
    }

    @Test
    void authorize() throws SQLException, IOException {
        playerDAO.savePlayer("artur", "artur@mail.ru", "artur");
        String input = "artur@mail.ru\nartur\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        Player player = playerService.authorize(scanner);
        Assert.assertNotNull(player);
    }

    @Test
    void playerExit() throws SQLException, IOException {
        Player player = playerDAO.savePlayer("user", "user@mail.ru", "user");
        playerService.playerExit(player);
        AuditDAO auditDAO = new AuditDAO();
        Assert.assertTrue(auditDAO.findByPlayerEmail(player.getEmail()).stream().filter(audit ->
            audit.getAuditType() == AuditType.EXIT
        ).anyMatch(audit -> true));
    }
}