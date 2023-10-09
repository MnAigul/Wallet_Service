package domain.repository;

import domain.model.Player;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, хранящий игроков {@link Player} в коллекции
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
public class PlayerRepository {

    /** Коллекция игроков, ключом которой явл-ся логин игрока, а значением - {@link Player}*/
    Map<String, Player> players= new HashMap<>();

    /**
     * Функция проверки существования логина в {@link PlayerRepository#players}
     * @param email логин
     */
    public boolean emailExists(String email) {
        return players.containsKey(email);
    }

    /**
     * Функция проверки существования игрока с данным логином и паролем в коллекции {@link PlayerRepository#players}
     * @param email логин
     * @param password пароль
     * @return {@link Player} Игрок
     */
    public Player playerExists(String email, String password) {
        Player result = null;
        if (players.containsKey(email)) {
            String passwordInMemory = players.get(email).getPassword();
            if (passwordInMemory.equals(password))
                result = players.get(email);
        }
        return result;
    }

    /**
     * Функция сохранения игрока в коллекции {@link PlayerRepository#players}
     * @param name имя игрока
     * @param email логин
     * @param password пароль
     * @return {@link Player} Игрок
     */
    public  Player savePlayer(String name, String email, String password) {
        Player result = null;
        if (!players.containsKey(email)) {
            Player newPlayer = new Player(name, email, password);
            players.put(email, newPlayer);
            result = newPlayer;
        }
        return result;
    }

    /**
     * Функция обновления баланса у {@link Player}
     * @param {@link Player}
     * @param sum новый баланс
     */
    public void updateBalance(Player player, BigDecimal sum) {
        player.setMoney(sum);
        players.replace(player.getEmail(), player);
    }

    /**
     * Функция возвращения игрока {@link Player} по логину
     * @param email логин
     * @return {@link Player} Игрок
     */
    public Player getPlayer(String email) {
        Player result = null;
        if (players.containsKey(email)) {
            result = players.get(email);
        }
        return result;
    }

}
