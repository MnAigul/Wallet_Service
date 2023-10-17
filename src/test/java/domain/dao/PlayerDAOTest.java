package domain.dao;

import domain.model.Player;
import domain.model.Role;
import infrastructure.DBUtils;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDAOTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.3-alpine"
    );

    private PlayerDAO playerDAO = new PlayerDAO();
    private Player player = new Player(2L, "user", "user", "user", Role.ROLE_USER, BigDecimal.valueOf(0));
    private Player player2 = new Player(3L, "user2", "user2", "user2", Role.ROLE_USER, BigDecimal.valueOf(0));

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
        playerDAO.savePlayer(player.getName(), player.getEmail(),  player.getPassword());
        playerDAO.savePlayer(player2.getName(), player2.getEmail(),  player2.getPassword());

    }



    @Test
    void savePlayer() throws SQLException, IOException {
        Player actual_player = playerDAO.savePlayer("dima", "dima", "dima");
        Assert.assertEquals(BigDecimal.valueOf(0), actual_player.getMoney());
        Assert.assertEquals(Optional.of(4L), Optional.of(actual_player.getId()));
        Assert.assertEquals(Role.ROLE_USER, actual_player.getRole());
    }

    @Test
    void findByEmail() throws SQLException {
        Player actual_player = playerDAO.findByEmail(player.getEmail());
        Player actual_player2 = playerDAO.findByEmail(player2.getEmail());
        Assert.assertEquals(player.getId(), actual_player.getId());
        Assert.assertEquals(player2.getId(), actual_player2.getId());
    }

    @Test
    void findByEmailAndPassword() throws SQLException, IOException {
        Player actual_player = playerDAO.findByEmailAndPassword(player.getEmail(), player.getPassword());
        Player actual_player2 = playerDAO.findByEmailAndPassword(player2.getEmail(), player2.getPassword());
        Assert.assertEquals(player.getId(), actual_player.getId());
        Assert.assertEquals(player2.getId(), actual_player2.getId());

    }


    @Test
    void updateBalance() throws SQLException, IOException {
        playerDAO.updateBalance(player, BigDecimal.valueOf(500));
        Player actual = playerDAO.findByEmail(player.getEmail());
        Assert.assertEquals(BigDecimal.valueOf(500), actual.getMoney());

    }
}