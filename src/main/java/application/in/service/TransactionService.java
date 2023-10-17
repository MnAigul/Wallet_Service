package application.in.service;


import domain.dao.AuditDAO;
import domain.dao.PlayerDAO;
import domain.dao.TransactionDAO;
import domain.model.AuditType;
import domain.model.EventType;
import domain.model.Player;
import domain.model.Transaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс, обрабатывающий транзакции (кредит/дебет) и предоставляющий просмотр всех транзакций
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
public class TransactionService {

    private AuditDAO auditDAO = new AuditDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private PlayerDAO playerDAO = new PlayerDAO();

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
     */
    public void allOperationsOfPlayer(Player player) {
        List<Transaction> transactions = transactionDAO.getAllOperationsOfPlayer(player.getId());
        transactions.forEach(transaction -> {
            System.out.println(transaction.getType() + " на сумму: " + transaction.getSum());
        });
    }




}
