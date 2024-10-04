package bashkirov.test.security.admin.service;

import bashkirov.test.security.admin.enumeration.Role;
import bashkirov.test.security.admin.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole(Role.ROLE_USER);
        person.setEnable(true);

        jdbcTemplate.update(
                "insert into person(name, lastname, email, username, password, role, is_enable) values (?,?,?,?,?,?,?)",
                person.getName(),
                person.getLastname(),
                person.getEmail(),
                person.getUsername(),
                person.getPassword(),
                person.getRole().toString(),
                person.isEnable()
        );
    }
}
