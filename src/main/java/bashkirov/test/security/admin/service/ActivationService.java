package bashkirov.test.security.admin.service;

import bashkirov.test.security.admin.exception.InvalidActivationKeyException;
import bashkirov.test.security.admin.model.Activation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivationService {
    private final JdbcTemplate jdbcTemplate;

    public void save(Activation activation) {
        jdbcTemplate.update(
                "insert into activation(key, email) values (?,?)",
                activation.getKey(),
                activation.getEmail()
        );
    }

    public String generateKey(String email) {
        StringBuilder sb = new StringBuilder();
        String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        int keyLength = (int) (15 + (Math.random() * 6));
        for (int i = 0; i < keyLength; i++) {
            int randomAlphabetIndex = (int) (alphabet.length() * Math.random());
            sb.append(alphabet.charAt(randomAlphabetIndex));
        }
        save(new Activation(sb.toString(), email));
        return sb.toString();
    }

    public void activate(String key) {
        Optional<Activation> optionalActivation = jdbcTemplate.query(
                "select * from activation where key = ?",
                new Object[]{key},
                new BeanPropertyRowMapper<>(Activation.class)
        ).stream().findAny();

        if (optionalActivation.isEmpty()) {
            throw new InvalidActivationKeyException("Failed to activate account");
        }
        Activation existedActivation = optionalActivation.get();

        jdbcTemplate.update(
                "update person set is_enable = true where email = ?",
                existedActivation.getEmail()
        );

        jdbcTemplate.update(
                "delete from activation where key = ?",
                key
        );
    }
}
