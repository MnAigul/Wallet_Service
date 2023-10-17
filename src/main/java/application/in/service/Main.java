package application.in.service;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.DBUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Главный класс, который взаимодействует с пользователем
 *
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class Main {

    private PlayerService playerService = new PlayerService();

    private TransactionService transactionService = new TransactionService();

    private AuditService auditService = new AuditService();

    /**
     * Главная функция
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        DBUtils.executeLiquiBase();
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        main.mainFunction(scanner);
        scanner.close();
    }

    /**
     * Функция, взаимодействующая с пользователем на старте (регистрация, авторизация, вход для админа)
     */
    public void mainFunction(Scanner scanner) throws SQLException, IOException {
        System.out.println("--Привет, это Wallet-Service!--");
        int n = 0;
        while (n != -1) {
            System.out.println("--Выбери дальнейшие действия:--");
            System.out.println("Нажми '1' для регистрации.");
            System.out.println("Нажми '2' для авторизации.");
            System.out.println("Нажми '3' для входа в качестве админа.");
            System.out.println("Нажми '-1' для выхода из приложения.");
            n = scanner.nextInt();
            switch (n) {
                case (1): {
                    playerService.registry(scanner);
                    break;
                }
                case (2): {
                    Player player = playerService.authorize(scanner);
                    if (player != null) {
                        personalAreaOfPlayer(player, scanner);
                    }
                    break;
                }
                case (3): {
                     Player player = playerService.authorize(scanner);
                     if (player != null) {
                         auditService.adminUserInterface(scanner);
                     }
                    break;
                }
            }
        }
    }


    /**
     * Функция, обрабатывающая действия игрока через Scanner
     */
    public void personalAreaOfPlayer(Player player, Scanner scanner) throws SQLException, IOException {
        int n = -1;
        do {
            System.out.println("-- Выбери дальнейшие действия: --");
            System.out.println("Нажми '1' для просмотра баланса.");
            System.out.println("Нажми '2' для снятия средств");
            System.out.println("Нажми '3' для кредита");
            System.out.println("Нажми '4' для просмотра истории пополнения/снятия ваших средств");
            System.out.println("Нажми '0' для выхода из личного кабинета");
            n = scanner.nextInt();
            switch (n) {
                case (1): {
                    System.out.println("-- Баланс = " + playerService.getBalance(player) + " --");
                    break;
                }
                case (2): {
                    System.out.println("Введите уникальный идентификатор транзакции: ");
                    Long id = scanner.nextLong();
                    System.out.println("Введите сумму снятия средств: ");
                    BigDecimal sum = scanner.nextBigDecimal();
                    Transaction transaction = new Transaction(id, player.getId(), TransactionType.DEBIT, sum);
                    transactionService.debitAndCredit(transaction, player);
                    break;
                }
                case (3): {
                    System.out.println("Введите уникальный идентификатор транзакции: ");
                    Long id = scanner.nextLong();
                    System.out.println("Введите сумму кредита: ");
                    BigDecimal sum = scanner.nextBigDecimal();
                    Transaction transaction = new Transaction(id, player.getId(), TransactionType.CREDIT, sum);
                    transactionService.debitAndCredit(transaction, player);
                    break;
                }
                case (4): {
                    transactionService.allOperationsOfPlayer(player);
                    break;
                }
                case (0): {
                    playerService.playerExit(player);
                }
            }
        } while (n != 0);

    }
}

