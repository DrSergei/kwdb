/**
 * Пакет для стилей.
 *
 * Поддерживает типовые сообщения, в будущем разные языки.
 */
package style

// Импорт.

/**
 * Типы сообщений.
 *
 * Используются для кодирования типовых сообщений.
 */
enum class Message {
    SUCCESSFUL_TRANSACTION, // успешная операция
    ERROR_SAVE, // ошибка при сохранении
    ERROR_LOG, // ошибка при логгировании
    ERROR_CLEAR, // ошибка при очистке
    ERROR_DOWNLOAD, // ошибка при загрузке
    ERROR_INSERT, // ошибка при вставке
    ERROR_DELETE, // ошибка при удаление
    ERROR_DELETE_ALL, // ошибка при удаление паттерна
    ERROR_FIND, // ошибка при поиске
    ERROR_FIND_ALL, // ошибка при поиске паттерна
    ERROR_EXIT, // ошибка при отключении базы данных
    ERROR_CREATE, // ошибка при создании
    ERROR_TRANSACTION, // ошибка при обращении к базе данных
    INVALID_ARGUMENTS, // неверные аргументы
    REMOTE_KEY, // ключ удален
    MISSING_KEY, // нет ключа
    MISSING_FILE, // нет файла
    INVALID_EXTENSION, // неправильное расширение
    ERROR_READ, // запрещено чтение
    ERROR_WRITE, // запрещена запись
}

/**
 * Служебная функция.
 *
 * Информирует пользователя об ошибках.
 */
fun report(message: Message): String {
    when (message) {
        Message.ERROR_SAVE -> return "An error occurred while saving, please try again.\n"
        Message.ERROR_LOG -> return "An error occurred while logging, please try again.\n"
        Message.ERROR_CLEAR -> return "An error occurred while cleaning, please try again.\n"
        Message.ERROR_DOWNLOAD -> return "An error occurred while loading, please try again.\n"
        Message.ERROR_INSERT -> return "An error occurred while inserting, please try again.\n"
        Message.ERROR_DELETE -> return "An error occurred while uninstalling, please try again.\n"
        Message.ERROR_DELETE_ALL -> return "An error occurred while deleting the pattern, please try again.\n"
        Message.ERROR_FIND -> return "An error occurred while searching, please try again.\n"
        Message.ERROR_FIND_ALL -> return "An error occurred while searching for a pattern, please try again.\n"
        Message.ERROR_EXIT -> return "An error occurred while disconnecting, please try again.\n"
        Message.ERROR_CREATE -> return "An error occurred while creating, please try again.\n"
        Message.ERROR_TRANSACTION -> return "An error occurred while accessing the database.\n"
        Message.SUCCESSFUL_TRANSACTION -> return "Successful operation.\n"
        Message.INVALID_ARGUMENTS -> return "Invalid arguments.\n"
        Message.REMOTE_KEY -> return "The key has been removed, but the memory has not been cleared.\n"
        Message.MISSING_KEY -> return "There is no key.\n"
        Message.MISSING_FILE -> return "Нет файла.\n"
        Message.INVALID_EXTENSION -> return "No file.\n"
        Message.ERROR_READ -> return "Read error.\n"
        Message.ERROR_WRITE -> return "Write error.\n"
    }
}