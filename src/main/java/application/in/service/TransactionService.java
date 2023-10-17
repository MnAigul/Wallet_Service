package application.in.service;


import domain.dao.AuditDAO;
import domain.dao.PlayerDAO;
import domain.dao.TransactionDAO;
import domain.model.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс, обрабатывающий транзакции (кредит/дебет) и предоставляющий просмотр всех транзакций
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@RequiredArgsConstructor
public class TransactionService {

    /** Объект AuditDAO для работы с базой данных {@link AuditDAO}*/
    private AuditDAO auditDAO = new AuditDAO();

    /** Объект TransactionDAO для работы с базой данных {@link TransactionDAO}*/
    private TransactionDAO transactionDAO = new TransactionDAO();

    /** Объект PlayerDAO для работы с базой данных {@link PlayerDAO}*/
    private PlayerDAO playerDAO = new PlayerDAO();

    /**
     * Функция обработки транзакий (дебета/кредита)
     * @param transaction {@link Transaction}
     * @param player {@link Player}
     * @return true, если операция прошла успешно и достаточно средств для операции дебета
     */
    public boolean debitAndCredit(Transaction transaction, Player player) throws SQLException, IOException {
        if (transaction.getType().toString().equals("DEBIT")) {
            if (player.getMoney().subtract(transaction.getSum()).compareTo(BigDecimal.valueOf(0)) >= 0) {
                if (transactionDAO.saveTransaction(transaction)) {
                    BigDecimal balance = player.getMoney();
                    BigDecimal newBalance = balance.subtract(transaction.getSum());
                    playerDAO.updateBalance(player, newBalance);
                    auditDAO.save(player.getId(), AuditType.DEBIT, EventType.SUCCESS);
                    System.out.println("Снятие денег прошло успешно!");
                    return true;
                } else {
                    auditDAO.save(player.getId(), AuditType.DEBIT, EventType.FAILED);
                }
            } else {
                auditDAO.save(player.getId(), AuditType.DEBIT, EventType.FAILED);
                System.out.println("Недостаточно средств!");
                return false;
            }
        } else {
            if (transactionDAO.saveTransaction(transaction)) {
                BigDecimal balance = player.getMoney();
                BigDecimal newBalance = balance.add(transaction.getSum());
                playerDAO.updateBalance(player, newBalance);
                auditDAO.save(player.getId(), AuditType.CREDIT, EventType.SUCCESS);
                System.out.println("Операция кредита прошла успешно!");
            } else {
                auditDAO.save(player.getId(), AuditType.CREDIT, EventType.FAILED);
            }
            return true;
        }
        return false;

    }

    /**
     * Функция, предоставляющая игроку информацию о всех его транзакциях
     * @param player {@link Player}
     */
    public void allOperationsOfPlayer(Player player) {
        List<Transaction> transactions = transactionDAO.getAllOperationsOfPlayer(player.getId());
        transactions.forEach(transaction -> {
            System.out.println(transaction.getType() + " на сумму: " + transaction.getSum());
        });
    }




}
