package frontend

// Стандартная библиотека.

// Собственные пакеты.
import backend.download
import parser.parser
import java.io.File


// Проверка адекватности переданного файла(существования, расширения и прав доступа).
fun checkFile(file : File) : Boolean {
    if (!file.exists()) {
        println("Нет файла ${file.absolutePath}")
        return false
    }
    if (file.extension != "dat") {
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
    val file = File("demo.dat")
    if (checkFile(file)) {
        val database = download(file)
        println("Вы открыли доступ к базе данных ${file.nameWithoutExtension}")
        println(database.data.keys)
        do {
            val line = readLine()
            if (line != null)
                parser(database, line)
            if (line == "exit")
                return
        } while (line != null)
    }

}