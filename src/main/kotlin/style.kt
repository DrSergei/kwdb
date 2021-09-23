/**
 * Пакет для стилей.
 *
 * Поддерживает типовые сообщения, в будущем разные языки.
 */
package style

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
    ERROR_FIND, // ошибка при поиске
    ERROR_EXIT, // ошибка при отключении базы данных
    ERROR_TRANSACTION, // ошибка при обращении к базе данных
    INVALID_ARGUMENTS, // неверные аргументы
    REMOTE_KEY, // ключ удален
    MISSING_KEY, // нет ключа
    MISSING_FILE, // нет файла
    INVALID_EXTENSION, // неправильное расширение
    ERROR_READ, // зпрещено чтение
}

/**
 * Служебная функция.
 *
 * Информирует пользователя об ошибках.
 */
fun report(message: Message) : String {
    when (message) {
        Message.ERROR_SAVE -> return "Произошла ошибка при сохранении, повторите попытку.\n"
        Message.ERROR_LOG -> return "Произошла ошибка при логгировании, повторите попытку.\n"
        Message.ERROR_CLEAR -> return "Произошла ошибка при очистке, повторите попытку.\n"
        Message.ERROR_DOWNLOAD -> return "Произошла ошибка при логгировании, повторите попытку.\n"
        Message.ERROR_INSERT -> return "Произошла ошибка при вставке, повторите попытку.\n"
        Message.ERROR_DELETE -> return "Произошла ошибка при удалении, повторите поытку.\n"
        Message.ERROR_FIND -> return "Произошла ошибка при поиске, повторите попытку.\n"
        Message.ERROR_EXIT -> return "Произошла ошибка при отключении, повторите попытку"
        Message.ERROR_TRANSACTION -> return "Произошла ошибка при обращеннии к базе данных.\n"
        Message.SUCCESSFUL_TRANSACTION -> return "Успешная операция.\n"
        Message.INVALID_ARGUMENTS -> return "Неверные аргументы.\n"
        Message.REMOTE_KEY -> return "Ключ удален, но память не очищена.\n"
        Message.MISSING_KEY -> return "Нет ключа.\n"
        Message.MISSING_FILE -> return "Нет файла.\n"
        Message.INVALID_EXTENSION -> return "Неверное расширение.\n"
        Message.ERROR_READ -> return "Ошибка на чтение.\n"
    }
}