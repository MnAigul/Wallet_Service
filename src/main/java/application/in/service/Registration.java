package application.in.service;

import domain.model.ActionType;
import domain.model.Audit;
import domain.model.Player;
import domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.Scanner;

/**
 * Класс, осуществляющий регитсрацию игрока, то есть добавление его в {@link PlayerRepository}
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@AllArgsConstructor
public class Registration {

    /**Поле репозитория для доступа к коллекции, хранящей игроков {@link PlayerRepository}*/
    PlayerRepository playerRepository;

    /**
     * Функция регистрации игрока
     */
    public void registry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите своё имя:");
        String name = scanner.next();
        System.out.println("Введите свой email:");
        String email = scanner.next();
        while (playerRepository.emailExists(email)) {
            System.out.println("Пользователь с таким email уже существует");
            System.out.println("Введите свой email:");
            email = scanner.nextLine();
        }
        System.out.println("Введите свой пароль:");
        String password = scanner.next();
        Player player = playerRepository.savePlayer(name, email, password);
        player.addNewActionToAudit(new Audit(ActionType.REGISTRATION));
        System.out.println("Регистрация прошла успешно!");
    }
}
