package domain.repository;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Класс, хранящий транзакции определенного игрока {@link Player}
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public class TransactionRepository {

    /** Коллекция транзакций, ключом которой явл-ся идентификационный номер id транзакции, а значением - {@link Transaction}*/
    Map<Long, Transaction> transactions = new HashMap<>();

    /**
     * Функция проверки существования транзакции с таким идентификационным номером
     * @param id идентификациионный номер
     * @return true если транзакция с таким id существует
     */
    public boolean transactionWithIdExists(Long id) {
        return transactions.containsKey(id);
    }

    /**
     * Функция сохранения транзакции в коллекции {@link TransactionRepository#transactions}
     * @param id идентификациионный номер
     * @param type {@link TransactionType} тип транзакции
     * @param sum сумма транзакции
     */
    public void saveTransaction(Long id, TransactionType type, BigDecimal sum) {
        if (!transactionWithIdExists(id)) {
            transactions.put(id, new Transaction(id, type, sum));
        }

    }


}
