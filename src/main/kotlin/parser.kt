/**
 * Пакет для парсера ввода.
 *
 * Разбирает сообщения пользователя по шаблону
 * (операция|арг1 или операция|арг1|арг2).
 */
package parser

//Стандартная библиотке.

// Собственные пакеты.

/**
 * Класс для хранения аргументов.
 *
 * Имени база данных, типа операции и ее аргументов.
 */
data class Arguments(val name: String, val operation: Operation, val arg: List<String>)

/**
 * Типы операций.
 *
 * Кодируют основыне операции с данными, в будущем будет расширяться.
 */
enum class Operation {
    INSERT, DELETE, FIND, CLEAR, RECOVERY, // операции с самой базой данной(вставка, удаление, поиск, очистка, восстановление)
    SAVE, DOWNLOAD, EXIT, // операции с диком(сохранение, загрузка, выход)
    ERROR, NULL, END // служебные(ошибка, пустой ввод)
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
            "find" -> return Arguments(name, Operation.FIND, args.drop(2)) // поиск
            "clear" -> return Arguments(name, Operation.CLEAR, args.drop(2)) // очистка
            "recovery" -> return Arguments(name, Operation.RECOVERY, args.drop(2)) // восстановление
            "download" -> return Arguments(name, Operation.DOWNLOAD, args.drop(2)) // загрузка
            "save" -> return Arguments(name, Operation.SAVE, args.drop(2)) // сохранение
            "exit" -> return Arguments(name, Operation.EXIT, args.drop(2)) // выход
            else -> return Arguments("", Operation.ERROR, listOf()) // ошибка
        }
    }
}