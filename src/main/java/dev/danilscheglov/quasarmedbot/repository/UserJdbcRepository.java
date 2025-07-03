package dev.danilscheglov.quasarmedbot.repository;

import dev.danilscheglov.quasarmedbot.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initializeTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS user (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        last_name TEXT NOT NULL,
                        first_name TEXT,
                        middle_name TEXT,
                        birthdate DATE,
                        pressure TEXT
                    )
                """;
        jdbcTemplate.execute(sql);
    }

    public void save(User user) {
        String sql = "INSERT INTO user (last_name, first_name, middle_name, birthdate, pressure) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getLastName(), user.getFirstName(), user.getMiddleName(), user.getBirthdate(), user.getPressure());
    }
}