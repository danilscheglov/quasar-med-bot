package dev.danilscheglov.quasarmedbot.util;

public class BotMessages {

    public static final String START_MESSAGE = """
            *Добро пожаловать\\!* 👋
            
            Для работы с ботом необходимо выполнить следующие шаги:
            
            1\\. Введите ваше *ФИО* \\(только буквы и пробелы\\)\\.
            Пример: Иванов Иван Иванович
            2\\. Введите *дату рождения* в формате `ДД\\.ММ\\.ГГГГ`\\.
            Пример: 01\\.01\\.1990
            3\\. Введите показания *давления* в формате `число/число`\\.
            Пример: 120/80
            
            *Перезапустить бота можно командой /start\\.*""";

    public static final String UNKNOWN_COMMAND = "❗ Неизвестная команда\\. Используйте /start для начала\\.";

    public static final String REQUEST_NAME = "❗ Пожалуйста, введите ваше *ФИО* \\(только буквы и пробелы, например, Иванов Иван Иванович\\)\\.";

    public static final String REQUEST_BIRTHDATE = "❗ Пожалуйста, введите *дату рождения* в формате `ДД\\.ММ\\.ГГГГ` \\(например, 01\\.01\\.1990\\)\\.";

    public static final String INVALID_BIRTHDATE_RANGE = "❗ Дата рождения вне допустимого диапазона (0–100 лет). Проверьте данные\\.";

    public static final String INVALID_BIRTHDATE_FORMAT = "❗ Ошибка: некорректный формат даты\\. Используйте `ДД\\.ММ\\.ГГГГ` \\(например, 01\\.01\\.1990\\)\\.";

    public static final String INVALID_NAME_PARTS = "❗ Введите ФИО в формате: Фамилия [Имя] [Отчество]";

    public static final String REQUEST_PRESSURE = """
            Теперь вы можете вводить показания *давления* в формате `число/число`\\.
            Пример: 120/80
            
            Диапазоны:
            • Систолическое \\(верхнее\\): 90–200
            • Диастолическое \\(нижнее\\): 60–120""";

    public static final String INVALID_PRESSURE_FORMAT = "❗ Пожалуйста, введите показания *давления* в формате `число/число` \\(например, 120/80\\)\\.";

    public static final String PRESSURE_OUT_OF_RANGE = """
            ❗ Введенные значения вне допустимого диапазона\\.
            • Систолическое: 90–200
            • Диастолическое: 60–120
            Пожалуйста, проверьте данные\\.""";

    public static final String PRESSURE_PARSE_ERROR = "❗ Ошибка: введите числовые значения в формате `число/число` \\(например, 120/80\\)\\.";

    public static final String NAME_RECORDED = "✅ *ФИО записано*: ";
    public static final String BIRTHDATE_RECORDED = "✅ *Дата рождения записана*: ";
    public static final String PRESSURE_RECORDED = "✅ *Показания давления записаны*: ";
    public static final String DATA_SENT = "✅ Данные отправлены";
}
