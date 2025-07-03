package dev.danilscheglov.quasarmedbot.service;

import org.springframework.stereotype.Service;

/**
 * Класс сервиса, ответственный за взаимодействие с API ЦРП для поиска записей пользователей.
 * Текущая реализация является заглушкой и возвращает фиксированный тестовый идентификатор.
 * Предназначен для интеграции с внешней системой ЦРП в производственной среде.
 *
 * @author Данил Щеглов (Danil Scheglov)
 * @since 2025-07-03
 */
@Service
public class CrpApiService {

    /**
     * @param lastName   Фамилия пользователя
     * @param firstName  Имя пользователя
     * @param middleName Отчество пользователя
     * @param birthdate  Дата рождения пользователя в формате, подходящем для обработки API
     * @return Строка, представляющая идентификатор результата поиска (например, "test-id-123").
     */
    public String search(String lastName, String firstName, String middleName, String birthdate) {
        return "test-id-123";
    }
}
