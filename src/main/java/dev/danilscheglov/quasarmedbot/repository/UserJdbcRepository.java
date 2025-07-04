package dev.danilscheglov.quasarmedbot.repository;

import dev.danilscheglov.quasarmedbot.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initializeTable() {
        String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        chat_id INTEGER NOT NULL,
                        username TEXT,
                        last_name TEXT NOT NULL,
                        first_name TEXT,
                        middle_name TEXT,
                        birthdate DATE,
                        crp_id TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """;
        jdbcTemplate.execute(createUsersTable);

        String createPressureReadingsTable = """
                    CREATE TABLE IF NOT EXISTS pressure_readings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        chat_id INTEGER NOT NULL,
                        pressure TEXT NOT NULL,
                        recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (chat_id) REFERENCES users(chat_id)
                    )
                """;
        jdbcTemplate.execute(createPressureReadingsTable);
    }

    public boolean existsUser(long chatId, String lastName, String firstName, String middleName, LocalDate birthdate) {
        String sql = """
                    SELECT COUNT(*) FROM users
                    WHERE chat_id = ? AND last_name = ? AND first_name = ? AND middle_name = ? AND birthdate = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, chatId, lastName, firstName, middleName, birthdate);
        return count != null && count > 0;
    }

    public void save(User user, long chatId, String username, String crpId, String pressure) {
        if (pressure != null && !pressure.isEmpty()) {
            String checkUserSql = "SELECT COUNT(*) FROM users WHERE chat_id = ?";
            if (jdbcTemplate.queryForObject(checkUserSql, Integer.class, chatId) == 0) {
                String insertUserSql = "INSERT INTO users (chat_id, username, last_name, first_name, middle_name, birthdate, crp_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertUserSql, chatId, username, user.getLastName(), user.getFirstName(), user.getMiddleName(), user.getBirthdate(), crpId);
            }

            String insertPressureSql = "INSERT INTO pressure_readings (chat_id, pressure) VALUES (?, ?)";
            jdbcTemplate.update(insertPressureSql, chatId, pressure);
        }
    }
}