package domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий Админа, который имеет доступ к аудиту
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
public class Admin {
    /** Поле логина админа */
    String email = "admin";
    /** Поле пароля админа */
    String password = "admin";


}
