/**
 * Пакет для обработки общения с пользователем.
 *
 * Поддерживает консольный ввод и базовые проверки.
 */
package frontend

// Стандартная библиотека.

// Собственные пакеты.
import backend.*
import parser.Arguments
import parser.Operation
import parser.parser
import style.Message
import style.report
import java.io.File

// Проверка адекватности переданного файла(существования, расширения и прав доступа).
fun checkFile(file: File): Boolean {
    if (!file.exists()) {
        println("Нет файла ${file.absolutePath}")
        return false
    }
    if (file.extension != "dat" && file.extension != "log") {
        println("${file.name} не текстовый файл")
        return false
    }
    if (!file.canRead()) {
        println("${file.name} не может быть прочитан")
        return false
    }
    return true
}

fun distributionInput(pool: Pool, arguments: Arguments) : String {
    val database = pool.data.get(arguments.name)
    if (database != null) {
        log(database, listOf(arguments.operation.name, arguments.arg).joinToString(":"))
        when (arguments.operation) {
            Operation.INSERT -> {
                if (arguments.arg.size == 2)
                    return report(insert(database, arguments.arg.get(0), arguments.arg.get(1)))
                else
                    return report(Message.INVALID_ARGUMENTS)
            }
            Operation.DELETE -> {
                if (arguments.arg.size == 1)
                    return report(delete(database, arguments.arg.get(0)))
                else
                    return report(Message.INVALID_ARGUMENTS)
            }
            Operation.FIND -> {
                if (arguments.arg.size == 1)
                    return find(database, arguments.arg.get(0)).first
                else
                    return report(Message.INVALID_ARGUMENTS)
            }
            Operation.CLEAR -> {
                return ""
            }
            Operation.DOWNLOAD -> {
                return ""
            }
            Operation.SAVE -> {
                val buffer = save(database)
                pool.data[arguments.name] = buffer.first
                return report(buffer.second)
            }
            Operation.EXIT -> {
                if (arguments.name == "pool") {
                    for (db in pool.data) {
                        exit(pool, db.key)
                    }
                    return "Конец работы"
                } else {
                    return report(exit(pool, arguments.name))
                }
            }
            Operation.ERROR -> {
                return report(Message.INVALID_ARGUMENTS)
            }
            Operation.NULL -> {
                return ""
            }
        }
    } else {
        return report(Message.INVALID_ARGUMENTS)
    }
}

// Работа со стандартным потоком ввода.
fun input() {
    val pool = Pool(mutableMapOf())
    pool.data.put("pool", Database(hashMapOf(), 0, File(""), File("")))
    pool.data.put("demo", download(File("demo.dat"), File("demo.log")).first)
    var request = ""
    while (request != null) {
        val arguments = parser(request)
        print(distributionInput(pool, arguments))
        request = readLine()!!
    }
}