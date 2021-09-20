/**
 * Пакет для парсера ввода.
 *
 * Разбирает сообщения пользователя по шаблону
 * (операция|арг1 или операция|арг1|арг2).
 */
package parser

//Стандартная библиотке.

// Собственные пакеты.
import backend.Database
import backend.Operation
import backend.save
import backend.transaction
import style.Message
import style.report


/**
 * Служебная функция.
 *
 * Преобразует строку запроса от пользователя в инструкции для базы данных.
 */
fun parser(database: Database, str : String) {
    val args = str.split('|')
    when (args.size) {
        0 -> {}
        1 -> {
            if (args[0] == "exit")
                save(database)
            else if (args[0] == "save")
                save(database)
            else
                report(Message.INVALID_ARGUMENTS)
        }
        2 -> {
            if (args[0] == "delete" || args[0] == "-")
                transaction(database, Operation.DELETE, args[1])
            else if (args[0] == "find" || args[0] == "?")
                transaction(database, Operation.FIND, args[1])
            else
                report(Message.INVALID_ARGUMENTS)
        }
        3 -> {
            if (args[0] == "insert" || args[0] == "+")
                transaction(database, Operation.INSERT, args[1], args[2])
            else
                report(Message.INVALID_ARGUMENTS)
        }
        else -> report(Message.INVALID_ARGUMENTS)
    }
}