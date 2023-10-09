package application.in.service;


import domain.model.ActionType;
import domain.model.Audit;
import domain.model.Player;
import domain.repository.PlayerRepository;
import domain.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Класс, обрабатывающий действия игрока в его личном кабинете
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Setter
@Getter
public class PlayerServiceImpl {

    /**Поле игрока {@link Player}*/
    Player player;
    /**Поле репозитория для доступа к коллекции, хранящей транзакции{@link TransactionRepository}*/
    TransactionServiceImpl transactionService;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param transactionService - {@link TransactionServiceImpl}
     * @param player - цена
     */
    PlayerServiceImpl(TransactionServiceImpl transactionService, Player player) {
        this.transactionService = transactionService;
        this.player = player;
    }

    /**
     * Функция, возвращающая баланс игрока и добавляющая данное действие к его аудиту
     */
    public BigDecimal getBalance() {
        player.addNewActionToAudit(new Audit(ActionType.CHECK_BALANCE));
        return player.getMoney();
    }

    /**
     * Функция, обрабатывающий действия игрока через Scanner
     */
    public void personalAreaOfPlayer() {
        int n = -1;
        do {
            System.out.println("-- Выбери дальнейшие действия: --");
            System.out.println("Нажми '1' для просмотра баланса.");
            System.out.println("Нажми '2' для снятия средств");
            System.out.println("Нажми '3' для кредита");
            System.out.println("Нажми '4' для просмотра истории пополнения/снятия ваших средств");
            System.out.println("Нажми '0' для выхода из личного кабинета");
            Scanner scanner = new Scanner(System.in);
            n = scanner.nextInt();
            switch (n) {
                case(1) : {
                    System.out.println("-- Баланс = " + getBalance() + " --");
                    break;
                } case(2): {
                    System.out.println("Введите уникальный идентификатор транзакции: ");
                    Long id = scanner.nextLong();
                    System.out.println("Введите сумму снятия средств: ");
                    BigDecimal sum = scanner.nextBigDecimal();
                    transactionService.debit(id, sum);
                    break;
                } case(3): {
                    System.out.println("Введите уникальный идентификатор транзакции: ");
                    Long id = scanner.nextLong();
                    System.out.println("Введите сумму кредита: ");
                    BigDecimal sum = scanner.nextBigDecimal();
                    transactionService.credit(id, sum);
                    break;
                } case (4) : {
                    transactionService.allOperationsOfPlayer();
                    break;
                } case (0): {
                    player.addNewActionToAudit(new Audit(ActionType.EXIT));
                }
            }
        } while (n != 0);
    }
}
