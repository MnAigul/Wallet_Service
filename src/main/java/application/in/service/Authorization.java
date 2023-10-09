package application.in.service;

import domain.model.ActionType;
import domain.model.Audit;
import domain.model.Player;
import domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Scanner;

/**
 * Класс, осуществляющий авторизацию игрока
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class Authorization {
    /**Поле репозитория для доступа к коллекции, хранящей игроков {@link PlayerRepository}*/
    PlayerRepository playerRepository;

    /**
     * Функция авторизации игрока
     */
    public Player authorize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите свой email:");
        String email = scanner.nextLine();
        System.out.println("Введите свой пароль:");
        String password = scanner.nextLine();
        Player player = null;
        player = playerRepository.playerExists(email, password);
        if (player == null) {
            System.out.println("Неверный email или пароль");
        } else {
            player.addNewActionToAudit(new Audit(ActionType.AUTHORISATION));
        }
        return player;
    }

}
