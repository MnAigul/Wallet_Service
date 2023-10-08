package domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс, описывающий объект журналирования каждого действия
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
public class Audit {
    /** Поле времени создания экземпляра объекта*/
    LocalDateTime time = LocalDateTime.now();
    /** Поле типа действия игрока {@link ActionType}*/
    ActionType type;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param type - тип действия {@link ActionType}
     */
    public Audit(ActionType type) {
        this.type = type;
    }

}
