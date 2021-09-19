package parser

//Стандартная библиотке.

// Собственные пакеты.
import backend.Database
import backend.Operation
import backend.save
import backend.transaction

// Преобразует строку пользователя в запрос к базе данных.
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
                println("Неверный аргумент ${args[0]}")
        }
        2 -> {
            if (args[0] == "delete" || args[0] == "-")
                transaction(database, Operation.DELETE, args[1])
            else if (args[0] == "find" || args[0] == "?")
                transaction(database, Operation.FIND, args[1])
            else
                println("Неверный аргумент ${args[0]}")
        }
        3 -> {
            if (args[0] == "insert" || args[0] == "+")
                transaction(database, Operation.INSERT, args[1], args[2])
            else
                println("Неверный аргумент ${args[0]}")
        }
        else -> println("Лишние аргументы")
    }
}