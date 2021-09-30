/**
 * Пакет для обработки ввода пользователя.
 *
 * Разбирает сообщения пользователя по шаблону
 * (операция|арг1 или операция|арг1|арг2).
 */
package parser

// Импорт.

/**
 * Класс для хранения аргументов.
 *
 * Имени база данных, типа операции и ее аргументов.
 */
data class Arguments(val name: String, val operation: Operation, val arg: List<String>)

/**
 * Типы операций.
 *
 * Кодируют основные операции с данными, в будущем будет расширяться.
 */
enum class Operation {
    INSERT, DELETE, DELETE_ALL, FIND, FIND_ALL, CLEAR, RECOVERY, SIZE, PRINT, // операции с самой базой данной
    // (вставка, удаление, удаление по паттерну, поиск, поиск паттерна, очистка, восстановление, размер, полный список)
    SAVE, DOWNLOAD, EXIT, CREATE, // операции с диском
    // (сохранение, загрузка, выход, создание)
    ERROR, NULL, END // служебные
    // (ошибка, пустой ввод, завершение работы)
}

/**
 * Служебная функция.
 *
 * Преобразует строку запроса от пользователя в инструкции для базы данных.
 */
fun parser(request: String): Arguments {
    if (request == "")
        return Arguments("", Operation.NULL, listOf())
    if (request == "end")
        return Arguments("", Operation.END, listOf())
    else {
        val args = request.split(':')
        if (args.size < 2)
            return Arguments("", Operation.ERROR, listOf())
        val name = args[0]
        when (args[1]) {
            "insert" -> return Arguments(name, Operation.INSERT, args.drop(2)) // вставка
            "delete" -> return Arguments(name, Operation.DELETE, args.drop(2)) // удаление
            "deleteAll" -> return Arguments(name, Operation.DELETE_ALL, args.drop(2)) // удаление паттерна
            "find" -> return Arguments(name, Operation.FIND, args.drop(2)) // поиск
            "findAll" -> return Arguments(name, Operation.FIND_ALL, args.drop(2)) // поиск паттерна
            "clear" -> return Arguments(name, Operation.CLEAR, args.drop(2)) // очистка
            "recovery" -> return Arguments(name, Operation.RECOVERY, args.drop(2)) // восстановление
            "print" -> return Arguments(name, Operation.PRINT, args.drop(2)) // полный список
            "size" -> return Arguments(name, Operation.SIZE, args.drop(2)) // размер
            "download" -> return Arguments(name, Operation.DOWNLOAD, args.drop(2)) // загрузка
            "save" -> return Arguments(name, Operation.SAVE, args.drop(2)) // сохранение
            "exit" -> return Arguments(name, Operation.EXIT, args.drop(2)) // выход
            "create" -> return Arguments(name, Operation.CREATE, args.drop(2)) // создание
            else -> return Arguments("", Operation.ERROR, listOf()) // ошибка
        }
    }
}