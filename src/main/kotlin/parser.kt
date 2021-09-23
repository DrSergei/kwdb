/**
 * Пакет для парсера ввода.
 *
 * Разбирает сообщения пользователя по шаблону
 * (операция|арг1 или операция|арг1|арг2).
 */
package parser

//Стандартная библиотке.

// Собственные пакеты.


data class Arguments(val name: String, val operation: Operation, val arg: List<String>)

/**
 * Типы операций.
 *
 * Кодируют основыне операции с данными, в будущем будет расширяться.
 *
 */
enum class Operation {
    INSERT, DELETE, FIND, CLEAR, // операции с самой базой данной
    SAVE, DOWNLOAD, EXIT, // операции с диком
    ERROR, NULL // служебные
}

/**
 * Служебная функция.
 *
 * Преобразует строку запроса от пользователя в инструкции для базы данных.
 */
fun parser(request: String): Arguments {
    if (request == "")
        return Arguments("", Operation.NULL, listOf())
    else {
        val args = request.split(':')
        if (args.size < 2)
            return Arguments("", Operation.ERROR, listOf())
        val name = args[0]
        when (args[1]) {
            "insert" -> return Arguments(name, Operation.INSERT, args.drop(2))
            "delete" -> return Arguments(name, Operation.DELETE, args.drop(2))
            "find" -> return Arguments(name, Operation.FIND, args.drop(2))
            "clear" -> return Arguments(name, Operation.CLEAR, args.drop(2))
            "download" -> return Arguments(name, Operation.DOWNLOAD, args.drop(2))
            "save" -> return Arguments(name, Operation.SAVE, args.drop(2))
            "exit" -> return Arguments(name, Operation.EXIT, args.drop(2))
            else -> return Arguments("", Operation.ERROR, listOf())
        }
    }
}