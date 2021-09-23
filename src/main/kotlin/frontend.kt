/**
 * Пакет для обработки общения с пользователем.
 *
 * Поддерживает консольный ввод и базовые проверки.
 */
package frontend

// Стандартная библиотека.

// Собственные пакеты.
import backend.Pool
import parser.Arguments
import parser.Operation
import parser.parser
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

fun distributionInput(pool: Pool, arguments: Arguments) {
    when (arguments.operation) {
        Operation.INSERT -> {
        }
        Operation.DELETE -> {
        }
        Operation.FIND -> {
        }
        Operation.CLEAR -> {
        }
        Operation.DOWNLOAD -> {
        }
        Operation.SAVE -> {
        }
        Operation.EXIT -> {
        }
        Operation.ERROR -> {
        }
        Operation.NULL -> {
        }
    }
}

// Работа со стандартным потоком ввода.
fun input() {
    val pool = Pool(mutableMapOf())
    var request = ""
    while (request != null) {
        val arguments = parser(request)
        distributionInput(pool, arguments)
        request = readLine()!!
    }
}