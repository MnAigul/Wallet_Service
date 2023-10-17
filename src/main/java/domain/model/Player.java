package domain.model;


import lombok.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Класс, описывающий игрока, пользователя Wallet-Service
 * @author Aigul Mingazova <aigul.mingazova.02@mail.ru>
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Player {
    /** Поле идентификатора игрока id*/
    private Long id;

    /** Поле имени*/
    private String name;

    /** Поле почты или логина*/
    private String email;

    /** Поле пароля*/
    private String password;

    /** Поле роли {@link Role}*/
    private Role role;

    /** Поле баланса игрока*/
    private BigDecimal money = BigDecimal.valueOf(0);


}
