/**
 * Пакет для реализации базы данных.
 *
 * Используется HashMap для поддержки быстрых операций с базой данных,
 * есть функции для загрузки и сохранения работы
 * (хранит данные в json).
 */
package backend

// Стандартная библиотека.

// Собственные пакеты.
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import style.Message
import style.report
import java.io.File

/**
 * Запись в базе данных.
 *
 * Класс для хранения данных.
 */
@Serializable
data class Mark(val data: String, var use : Boolean)

/**
 * Сама база данных.
 *
 *  Класс для хранения данных.
 */
data class Database(val data : HashMap<String, Mark>, var counter : Int, val dataFile : File, val logFile : File)

/**
 * Типы операций.
 *
 * Кодируют основыне операции с данными, в будущем будет расширяться.
 *
 */
enum class Operation {
    DELETE, INSERT, FIND
}


/**
 * Служебная функция.
 *
 * Исполняет обращения к базе данных.
 */
fun transaction(database: Database, operation : Operation, vararg args : String) {
    try {
        when (operation) {
            Operation.DELETE -> {
                if (args.size == 1) {
                    val mark = database.data[args[0]]
                    if (mark == null)
                        report(Message.MISSING_KEY)
                    else if (!mark.use)
                        report(Message.REMOTE_KEY)
                    else
                        mark.use = false
                } else
                    report(Message.INVALID_ARGUMENTS)
            }
            Operation.FIND -> {
                if (args.size == 1) {
                    val mark = database.data[args[0]]
                    if (mark == null || !mark.use)
                        report(Message.MISSING_KEY)
                    else
                        println(mark.data)
                } else
                    report(Message.INVALID_ARGUMENTS)
            }
            Operation.INSERT -> {
                if (args.size == 2)
                    database.data[args[0]] = Mark(args[1], true)
                else
                    report(Message.INVALID_ARGUMENTS)
            }
        }
    } catch (e : Exception) {
        report(Message.ERROR_TRANSACTION)
    }
}

/**
 * Служебная функция.
 *
 * Логгирует операции, исполненные на базе данных.
 */
fun log(database: Database, line : String) : Boolean{
    try {
        database.logFile.appendText(line + "\n")
        return true
    } catch (e : Exception) {
        report(Message.ERROR_LOG)
        return false
    }
}

/**
 * Служебная функция.
 *
 * Очищает базу данных от удаленных значений, перестраивает структуру данных.
 */
fun clear(database: Database) : Database{
    try {
        return Database(database.data.filter { it.value.use } as HashMap<String, Mark>, 0, database.dataFile, database.logFile)
    } catch (e : Exception) {
        report(Message.ERROR_CLEAR)
        return database
    }
}

/**
* Служебная функция.
*
* Загружает базу данных и лог файл.
*/
fun download(dataFile : File, logFile: File) : Database {
    try {
        val json = dataFile.readText()
        val buffer = Json.decodeFromString<HashMap<String, Mark>>(json)
        logFile.writeText("")
        return Database(buffer, 0, dataFile, logFile)
    } catch (e : Exception) {
        report(Message.ERROR_DOWNLOAD)
        return Database(hashMapOf(), 0, dataFile, logFile)
    }
}

/**
 * Служебная функция.
 *
 * Сохраняет базу данных из оперативной памяти на диск.
 */
fun save(database : Database) {
    try {
        val json = Json.encodeToString(clear(database).data)
        database.dataFile.writeText(json)
    } catch (e : Exception) {
        report(Message.ERROR_SAVE)
    }
}