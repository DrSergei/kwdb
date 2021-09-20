/**
 * Пакет для обработки общения с пользователем.
 *
 * Поддерживает консольный ввод и базовые проверки.
 */
package frontend

// Стандартная библиотека.

// Собственные пакеты.
import backend.download
import backend.log
import parser.parser
import java.io.File

// Проверка адекватности переданного файла(существования, расширения и прав доступа).
fun checkFile(file : File) : Boolean {
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

// Работа со стандартным потоком ввода.
fun input() {
    val d = File("demo.dat")
    val l = File("demo.log")
    if (checkFile(d) && checkFile(l)) {
        val database = download(d, l)
        do {
            val line = readLine()
            if (line != null) {
                if (log(database, line))
                    parser(database, line)
            }
            if (line == "exit")
                return
        } while (line != null)
    }

}