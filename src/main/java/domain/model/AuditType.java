package domain.model;

/**
 * Enum, описывающий тип аудита
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
public enum AuditType {
    /** Тип регистрации*/
    REGISTRATION,

    /** Тип авторизации*/
    AUTHORISATION,

    /** Тип выхода из аккаунта */
    EXIT,

    /** Тип дебета/снятия средств */
    DEBIT,

    /** Тип кредита */
    CREDIT,

    /** Тип просмотра баланса */
    CHECK_BALANCE
}
