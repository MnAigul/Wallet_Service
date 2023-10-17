package application.in.service;


import domain.dao.AuditDAO;
import domain.dao.PlayerDAO;
import domain.model.AuditType;
import domain.model.EventType;
import domain.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Класс, обрабатывающий действия игрока в его личном кабинете
 *
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */

public class PlayerService {

    private PlayerDAO playerDAO = new PlayerDAO();
    private AuditDAO auditDAO = new AuditDAO();

    public BigDecimal getBalance(Player player) throws SQLException, IOException {
        auditDAO.save(player.getId(), AuditType.CHECK_BALANCE, EventType.SUCCESS);
        return playerDAO.findByEmail(player.getEmail()).getMoney();
    }

    public void registry(Scanner scanner) throws SQLException, IOException {
        System.out.println("Введите своё имя:");
        String name = scanner.next();

        System.out.println("Введите свой email:");
        String email = scanner.next();

        boolean fail = false;
        while (playerDAO.findByEmail(email) != null) {
            fail = true;
            System.out.println("Пользователь с таким email уже существует");
            System.out.println("Введите свой email:");
            email = scanner.nextLine();
        }
        System.out.println("Введите свой пароль:");
        String password = scanner.next();
        Player player = playerDAO.savePlayer(name, email, password);
        if (fail) {
            auditDAO.save(player.getId(), AuditType.REGISTRATION, EventType.FAILED);
        }
        auditDAO.save(player.getId(), AuditType.REGISTRATION, EventType.SUCCESS);
        System.out.println("Регистрация прошла успешно!");
    }

    public Player authorize(Scanner scanner) throws SQLException, IOException {
        Player player = null;
        System.out.println("Введите свой email:");
        String email = scanner.next();
        System.out.println("Введите свой пароль:");
        String password = scanner.next();
        player = playerDAO.findByEmailAndPassword(email, password);
        if (player == null) {
            auditDAO.save(player.getId(), AuditType.AUTHORISATION, EventType.FAILED);
            System.out.println("Неверный email или пароль");
        } else {
            auditDAO.save(player.getId(), AuditType.AUTHORISATION, EventType.SUCCESS);
        }
        return player;
    }

    public void playerExit(Player player) throws IOException {
        auditDAO.save(player.getId(), AuditType.EXIT, EventType.SUCCESS);
    }
}
