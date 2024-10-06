package bashkirov.test.security.admin.service;

import bashkirov.test.security.admin.enumeration.Role;
import bashkirov.test.security.admin.model.Person;
import bashkirov.test.security.admin.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = getOptionalPersonByUsername(username);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException("Failed to find person with username = " + username);
        }
        Person existedPerson = optionalPerson.get();

        return new PersonDetails(existedPerson);
    }

    public Optional<Person> getOptionalPersonByUsername(String username) {
        return jdbcTemplate.query(
                "select * from person where username = ?",
                new Object[]{username},
                getPersonRowMapper()
        ).stream().findAny();
    }

    public Optional<Person> getOptionalUserByEmail(String email) {
        return jdbcTemplate.query(
                "select * from person where email = ?",
                new Object[]{email},
                getPersonRowMapper()
        ).stream().findAny();
    }

    public static RowMapper<Person> getPersonRowMapper() {
        return (rs, rowNum) -> {
            Person person = new Person();
            person.setName(rs.getString("name"));
            person.setLastname(rs.getString("lastname"));
            person.setEmail(rs.getString("email"));
            person.setUsername(rs.getString("username"));
            person.setPassword(rs.getString("password"));
            person.setRole(Role.valueOf(rs.getString("role")));
            person.setEnable(rs.getBoolean("is_enable"));
            return person;
        };
    }

    public void addAdmin(int id) {
        jdbcTemplate.update(
                "update person set role = 'ROLE_ADMIN' where id = ?",
                id
        );
    }

    public void removeAdmin(int id) {
        jdbcTemplate.update(
                "update person set role = 'ROLE_USER' where id = ?",
                id
        );
    }

    public List<Person> getAllPersonAdmin() {
        return jdbcTemplate.query(
                "select * from person where role = 'ROLE_ADMIN'",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public List<Person> getAllPersonUser() {
        return jdbcTemplate.query(
                "select * from person where role = 'ROLE_USER'",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }


}
