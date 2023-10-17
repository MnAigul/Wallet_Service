package application.in.service;

import domain.dao.PlayerDAO;
import infrastructure.DBUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AuditServiceTest {

    private AuditService auditService = new AuditService();

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.3-alpine"
    );


    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        postgres.start();
        DBUtils connectionProvider = new DBUtils(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        DBUtils.executeLiquiBase();
        String input = "aigul\naigul@mail.ru\naigul\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        (new PlayerService()).registry(scanner);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }



    @Test
    void getEmailsOfAllPlayers() {
        auditService.getEmailsOfAllPlayers();
        Assert.assertEquals("aigul@mail.ru\n", output.toString());

    }

    @Test
    void getAllActionsOfPlayer() {
        auditService.getAllActionsOfPlayer("user");
        Assert.assertEquals("There is no player with this email!\n", output.toString());
    }
}