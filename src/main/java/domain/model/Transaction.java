package domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Класс, описывающий транзакцию, проведенную игроком
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    /** Идентификатор транзакции*/
    private Long id;

    /** Идентификатор пользователя, совершившего транзакцию*/
    private Long playerId;

    /** Поле типа транзакции {@link TransactionType}*/
    private TransactionType type;

    /** Поле суммы транзакции, на которую была совершена операция*/
    private BigDecimal sum;
}
