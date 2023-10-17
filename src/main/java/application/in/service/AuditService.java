package application.in.service;


import domain.dao.AuditDAO;
import domain.model.Audit;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Scanner;

/**
 * Класс, обрабатывающий просмотр всех зарегистрированных логинов в сисеме, а также просмотр всех действий пользователя
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@NoArgsConstructor
public class AuditService {

    /** Объект AuditDAO для работы с базой данных {@link AuditDAO}*/
    private AuditDAO auditDAO = new AuditDAO();

    /**
     * Функция, обрабатывающая запросы админа (получение всех зарегистрированных пользователей и аудит действий игрока)
     * @param scanner для ввода данных
     */
    public void adminUserInterface(Scanner scanner) {
        int n = 0;
        do {
            System.out.println("--Выбери дальнейшие действия:--");
            System.out.println("Нажми '0' для выхода из личного кабинета.");
            System.out.println("Нажми '1' для просмотра email-ов всех зарегистрированных игроков.");
            System.out.println("Нажми '2' для аудита дйствий игрока.");
            n = scanner.nextInt();
            switch (n) {
                case(1) : {
                    getEmailsOfAllPlayers();
                    break;
                } case(2): {
                    System.out.println("Введите email игрока:");
                    String email = scanner.next();
                    getAllActionsOfPlayer(email);
                    break;
                }
            }
        } while (n != 0);
    }

    /**
     * Функция получения логинов всех зарегистрированных пользователей
     */
    public void getEmailsOfAllPlayers() {
        auditDAO.findAllPlayersEmails().forEach(email -> {
            System.out.println(email);
        });
    }

    /**
     * Функция просмотра аудита определенного пользователя
     * @param email логин данного пользователя, по которому будет осуществляться аудит
     */
    public void getAllActionsOfPlayer(String email) {
        List<Audit> audits = auditDAO.findByPlayerEmail(email);
        if (audits.size() == 0) {
            System.out.println("There is no player with this email!");
        } else {
            audits.forEach(audit -> {
                System.out.println(audit.getAuditType() + " " + audit.getEventType() + " " + audit.getTime());
            });
        }
    }
}
