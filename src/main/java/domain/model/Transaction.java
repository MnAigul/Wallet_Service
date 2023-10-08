package domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class Transaction {

    /** Идентификатор транзакции*/
    Long id;
    /** Поле типа транзакции {@link TransactionType}*/
    TransactionType type;

    /** Поле суммы транзакции, на которую была совершена операция*/
    BigDecimal sum;
}
