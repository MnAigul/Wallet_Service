package domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Audit {

    private Long id;
    private Long playerId;
    private AuditType auditType;
    private EventType eventType;

    /** Поле времени создания экземпляра объекта*/
    private Time time;

    public Audit(Long playerId, AuditType auditType, EventType eventType) {
        this.playerId = playerId;
        this.auditType = auditType;
        this.eventType = eventType;
    }
}
