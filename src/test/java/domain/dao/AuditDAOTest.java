package domain.dao;

import domain.model.Audit;
import domain.model.AuditType;
import domain.model.EventType;
import domain.model.Player;
import infrastructure.DBUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


class AuditDAOTest {

    private AuditDAO auditDAO;

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
        auditDAO = new AuditDAO();

    }



    @Test
    void save() throws IOException, SQLException {
        PlayerDAO playerDAO = new PlayerDAO();
        Player player = playerDAO.savePlayer("user2", "user2", "user2");
        boolean res = auditDAO.save(2L, AuditType.REGISTRATION, EventType.SUCCESS);
        boolean res2 = auditDAO.save(2L, AuditType.REGISTRATION, EventType.SUCCESS);
        Assert.assertEquals(true, res);
        Assert.assertEquals(true, res);
    }

    @Test
    void findByPlayerEmail() throws IOException, SQLException {
        PlayerDAO playerDAO = new PlayerDAO();
        Player player = playerDAO.savePlayer("user", "user", "user");
        auditDAO.save(player.getId(), AuditType.REGISTRATION, EventType.SUCCESS);
        auditDAO.save(player.getId(), AuditType.EXIT, EventType.SUCCESS);
        List<Audit> audits = auditDAO.findByPlayerEmail(player.getEmail());
        Assert.assertEquals(2, audits.size());
    }

}