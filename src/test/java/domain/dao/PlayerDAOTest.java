package domain.dao;

import infrastructure.DBUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDAOTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.3-alpine"
    );

    PlayerDAO playerDAO;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

//    @BeforeEach
//    void setUp() {
//
//        DBUtils connectionProvider = new DBUtils(
//                postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword()
//        );
//        customerService = new CustomerService(connectionProvider);
//    }

    @Test
    void findByEmail() {
    }

    @Test
    void findByEmailAndPassword() {
    }

    @Test
    void savePlayer() {
    }

    @Test
    void updateBalance() {
    }
}