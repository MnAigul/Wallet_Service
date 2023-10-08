package application.in.service;


import domain.model.*;
import domain.repository.PlayerRepository;
import domain.repository.TransactionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Класс, обрабатывающий транзакции (кредит/дебет) и предоставляющий просмотр всех транзакций
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
public class TransactionServiceImpl {

    /**Поле репозитория для доступа к коллекции, хранящей транзакции{@link TransactionRepository}*/
    TransactionRepository transactionRepository;

    /**Поле репозитория для доступа к коллекции, хранящей игроков {@link PlayerRepository}*/
    PlayerRepository playerRepository;
    /**Поле игрока, который проводит транзации */
    Player player;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param transactionRepository - {@link TransactionRepository}
     * @param playerRepository - {@link PlayerRepository}
     * @param player - игрок
     */
    TransactionServiceImpl(TransactionRepository transactionRepository, PlayerRepository playerRepository, Player player) {
        this.player = player;
        this.transactionRepository = transactionRepository;
        this.playerRepository = playerRepository;
    }


    /**
     * Функция проведения транзакции - дебета
     * @param id идентификациионный номер
     * @param sum сумма транзакции
     * @return true транзакция дебит прошла успешно
     */
    public boolean debit(Long id, BigDecimal sum) {
        Transaction transaction = new Transaction(id, TransactionType.DEBIT, sum);
        if (!transactionRepository.transactionWithIdExists(transaction.getId())) {
            if (player.getMoney().subtract(sum).compareTo(BigDecimal.valueOf(0)) >= 0) {
                transactionRepository.saveTransaction(transaction.getId(), transaction.getType(), transaction.getSum());
                player.addNewTransactionToPlayer(transaction);
                BigDecimal balance = player.getMoney();
                BigDecimal newBalance = balance.subtract(sum);
                playerRepository.updateBalance(player, newBalance);
                player.addNewActionToAudit(new Audit(ActionType.DEBIT));
                System.out.println("Снятие денег прошло успешно!");
                return true;
            } else {
                System.out.println("Недостаточно средств!");
                return false;
            }
        } else {
            System.out.println("Данный идентификатор транзакции уже существует!");
            return false;
        }
    }

    /**
     * Функция проведения транзакции - кредита
     * @param id идентификациионный номер
     * @param sum сумма транзакции
     * @return true транзакция кредит прошла успешно
     */
    public boolean credit(Long id, BigDecimal sum) {
        Transaction transaction = new Transaction(id, TransactionType.CREDIT, sum);
        if (!transactionRepository.transactionWithIdExists(transaction.getId())) {
            transactionRepository.saveTransaction(transaction.getId(), transaction.getType(), transaction.getSum());
            player.addNewTransactionToPlayer(transaction);
            BigDecimal balance = player.getMoney();
            BigDecimal newBalance = balance.add(sum);
            playerRepository.updateBalance(player, newBalance);
            player.addNewActionToAudit(new Audit(ActionType.CREDIT));
            System.out.println("Операция кредита прошла успешно!");
            return true;
        } else {
            System.out.println("Данный идентификатор транзакции уже существует!");
            return false;
        }
    }

    /**
     * Функция, предоставляющая игроку информацию о всех его транзакциях
     */
    public void allOperationsOfPlayer() {
        player.getPlayerTransactions().forEach((transaction -> {
            System.out.println(transaction.getType() + " на сумму: " + transaction.getSum());
        }));
    }




}
