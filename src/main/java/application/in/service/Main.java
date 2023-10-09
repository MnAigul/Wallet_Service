package application.in.service;

import domain.model.Player;
import domain.repository.PlayerRepository;
import domain.repository.TransactionRepository;

import java.util.Scanner;

/**
 * Главный класс, который взаимодействует с пользователем
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class Main {
    /**Поле репозитория для доступа к коллекции, хранящей игроков {@link PlayerRepository}*/
    PlayerRepository playerRepository = new PlayerRepository();
    /**Поле репозитория для доступа к коллекции, хранящей транзакции{@link TransactionRepository}*/
    TransactionRepository transactionRepository = new TransactionRepository();

    /**
     * Главная функция
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.mainFunction();
    }

    /**
     * Функция, взаимодействующая с пользователем на старте (регистрация, авторизация, вход для админа)
     */
    public void mainFunction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--Привет, это Wallet-Service!--");
        int n = 0;
        while (n != -1) {
            System.out.println("--Выбери дальнейшие действия:--");
            System.out.println("Нажми '1' для регистрации.");
            System.out.println("Нажми '2' для авторизации.");
            System.out.println("Нажми '3' для входа в качестве админа.");
            System.out.println("Нажми '-1' для выхода из приложения.");
            n = scanner.nextInt();
            switch(n) {
                case(1): {
                    Registration registration = new Registration(playerRepository);
                    registration.registry();
                    break;
                } case (2): {
                    Authorization authorization = new Authorization(playerRepository);
                    Player player = authorization.authorize();
                    if (player != null) {
                        PlayerServiceImpl playerService = new PlayerServiceImpl(new TransactionServiceImpl(transactionRepository, playerRepository, player), player);
                        playerService.setPlayer(player);
                        playerService.personalAreaOfPlayer();
                    }
                    break;
                } case (3): {
                    scanner = new Scanner(System.in);
                    System.out.println("Введите email админа ='admin':");
                    String email = scanner.nextLine();
                    System.out.println("Введите пароль админа ='admin':");
                    String password = scanner.nextLine();
                    AdminService adminService = new AdminService(playerRepository);
                    adminService.authorizeAdmin(email, password);
                    break;
                }
            }

        }

    }

}
