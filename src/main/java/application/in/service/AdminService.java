package application.in.service;

import domain.model.Admin;
import domain.model.Player;
import domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.Scanner;

/**
 * Класс, обрабатывающий вход админа и просмотр всех зарегистрированных логинов в сисеме, а также просмотр всех действий пользователя
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@AllArgsConstructor
public class AdminService {
    /**Поле репозитория для доступа к коллекции, хранящей игроков {@link PlayerRepository}*/
    PlayerRepository playerRepository;

    /**
     * Функция авторизации админа
     * @param email логин админа
     * @param password пароль админа
     */
    public void authorizeAdmin(String email, String password) {
        Admin admin = new Admin();
        if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
            System.out.println("--Вы вошли как админ--");
            adminUserInterface();
        } else {
            System.out.println("Неверный email или пароль");
        }
    }

    /**
     * Функция, обрабатывающая запросы админа (получение всех зарегистрированных пользователей и аудит действий игрока)
     */
    public void adminUserInterface() {
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        do {
            System.out.println("--Выбери дальнейшие действия:--");
            System.out.println("Нажми '0' для выхода из личного кабинета.");
            System.out.println("Нажми '1' для просмотра email-ов всех зарегистрированных игроков.");
            System.out.println("Нажми '2' для аудита дйствий игрока.");
            n = scanner.nextInt();
            switch (n) {
                case(1) : {
                    getEmailsOfAllPlayers();
                    break;
                } case(2): {
                    System.out.println("Введите email игрока:");
                    String email = scanner.next();
                    getAllActionsOfPlayer(email);
                    break;
                }
            }
        } while (n != 0);
    }

    /**
     * Функция получения логинов всех зарегистрированных пользователей
     */
    public void getEmailsOfAllPlayers() {
        playerRepository.getPlayers().forEach((email, player) -> {
            System.out.println(email);
        });
    }

    /**
     * Функция просмотра аудита определенного пользователя
     * @param email логин данного пользователя, по которому будет осуществляться аудит
     */
    public void getAllActionsOfPlayer(String email) {
        Player player = playerRepository.getPlayer(email);
        if (player == null) {
            System.out.println("There is no player with this email!");
        } else {
            player.getAudit().forEach((audit -> {
                System.out.println(audit.getType() + " " + audit.getTime());
            }));
        }
    }
}
