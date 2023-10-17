package application.in.service;


import domain.dao.AuditDAO;
import domain.dao.PlayerDAO;
import domain.model.AuditType;
import domain.model.EventType;
import domain.model.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Класс, обрабатывающий действия игрока по авторизации, регистрации и получения баланса
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class PlayerService {

    /** Объект PlayerDAO для работы с базой данных {@link PlayerDAO}*/
    private PlayerDAO playerDAO = new PlayerDAO();

    /** Объект AuditDAO для работы с базой данных {@link AuditDAO}*/
    private AuditDAO auditDAO = new AuditDAO();

    /**
     * Функция, предоставляющая игроку информацию о его текущем балансе и записывающая дейстие просмотра балагса в БД
     * @param player {@link Player}
     * @return balance
     */
    public BigDecimal getBalance(Player player) throws SQLException, IOException {
        auditDAO.save(player.getId(), AuditType.CHECK_BALANCE, EventType.SUCCESS);
        return playerDAO.findByEmail(player.getEmail()).getMoney();
    }

    /**
     * Функция, осуществляюшая регистрацию пользователя
     * @param scanner для ввода данных
     */
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

    /**
     * Функция, осуществляюшая авторизацию пользователя
     * @param scanner для ввода данных
     * @return {@link Player}
     */
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

    /**
     * Функция записывающая в аудит запись выход пользователя
     * @param {@link Player}
     */
    public void playerExit(Player player) throws IOException {
        auditDAO.save(player.getId(), AuditType.EXIT, EventType.SUCCESS);
    }
}
