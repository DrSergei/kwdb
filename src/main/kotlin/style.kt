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
    ERROR_SAVE, // ошибка при сохранении
    ERROR_LOG, // ошибка при логгировании
    ERROR_CLEAR, // ошибка при очистке
    ERROR_DOWNLOAD, // ошибка при загрузке
    ERROR_TRANSACTION, // ошибка при обращении к базе данных
    INVALID_ARGUMENTS, // неверные аргументы
    REMOTE_KEY, // ключ удален
    MISSING_KEY, // нет ключа
    MISSING_FILE, // нет файла
    INVALID_EXTENSION, // неправильное расширение
    ERROR_READ // зпрещено чтение
}

/**
 * Служебная функция.
 *
 * Информирует пользователя об ошибках.
 */
fun report(message: Message) {
    when (message) {
        Message.ERROR_SAVE -> {println("Произошла ошибка при сохранении, повторите попытку.")}
        Message.ERROR_LOG -> {println("Произошла ошибка при логгировании, повторите попытку.")}
        Message.ERROR_CLEAR -> {println("Произошла ошибка при очистке, повторите попытку.")}
        Message.ERROR_DOWNLOAD -> {println("Произошла ошибка при логгировании, повторите попытку.")}
        Message.ERROR_TRANSACTION -> {println("Произошла ошибка при обращеннии к базе данных.")}
        Message.INVALID_ARGUMENTS -> {println("Неверные аргументы.")}
        Message.REMOTE_KEY -> {println("Ключ удален, но память не очищена.")}
        Message.MISSING_KEY -> {println("Нет ключа.")}
        Message.MISSING_FILE -> {println("Нет файла.")}
        Message.INVALID_EXTENSION -> {println("Неверное расширение.")}
        Message.ERROR_READ -> {println("Ошибка на чтение.")}
    }
}