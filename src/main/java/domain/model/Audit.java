package domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Time;

/**
 * Класс, описывающий аудит действий пользоватедей приложения
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Audit {
    /** Поле идентификатора аудита id*/
    private Long id;

    /** Поле идентификатора игрока id*/
    private Long playerId;

    /** Поле типа аудита {@link AuditType}*/
    private AuditType auditType;

    /** Поле типа исхода (SUCCESS/FAIL) {@link EventType}*/
    private EventType eventType;

    /** Поле времени создания экземпляра объекта*/
    private Time time;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param playerId - id игрока
     * @param auditType - тип действия {@link AuditType}
     * @param eventType - исход действия {@link EventType}
     */
    public Audit(Long playerId, AuditType auditType, EventType eventType) {
        this.playerId = playerId;
        this.auditType = auditType;
        this.eventType = eventType;
    }
}
