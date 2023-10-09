package domain.model;


import lombok.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Класс, описывающий игрока, пользователя Wallet-Service
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class Player {
    /** Поле имени*/
    String name;
    /** Поле почты или логина*/
    String email;
    /** Поле пароля*/
    String password;
    /** Поле баланса игрока*/
    BigDecimal money = BigDecimal.valueOf(0);
    /** Поле транзакций, проведенных игроком*/
    Set<Transaction> playerTransactions = new HashSet<>();

    /** Поле журналирования всех действий игрока*/
    Set<Audit> audit = new LinkedHashSet<>();

    /**
     * Функция добавления новой транзакции к {@link Player#playerTransactions}
     */
    public void addNewTransactionToPlayer(Transaction transaction) {
        this.playerTransactions.add(transaction);
    }

    /**
     * Функция добавления нового лога к {@link Player#audit}
     */
    public void addNewActionToAudit(Audit audit) {
        this.audit.add(audit);
    }

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param name - имя
     * @param email - логин
     * @param password - пароль
     */
    public Player(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }



}
