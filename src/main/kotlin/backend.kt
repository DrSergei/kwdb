/**
 * Пакет для реализации базы данных.
 *
 * Используется HashMap для поддержки быстрых операций с базой данных,
 * есть функции для загрузки и сохранения работы
 * (хранит данные в json).
 */
package backend

// Стандартная библиотека.
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

// Собственные пакеты.
import style.*


/**
 * Запись в базе данных.
 *
 * Класс для хранения данных.
 */
@Serializable
data class Mark(val data: String, var use: Boolean)

/**
 * Сама база данных.
 *
 *  Класс для хранения данных.
 */
data class Database(
    val name: String,
    val data: HashMap<String, Mark>,
    var counter: Int,
    val dataFile: File,
    val logFile: File,
)

/**
 * Пул баз данных
 *
 *  Класс для хранения запущенных баз данных.
 */
data class Pool(val data: MutableMap<String, Database>)


/**
 * Служебная функция.
 *
 * Вставляет пару ключ, значение в базу данных
 */
fun insert(database: Database, key: String, value: String): Message {
    try {
        database.data[key] = Mark(value, true)
        return Message.SUCCESSFUL_TRANSACTION
    } catch (e: Exception) {
        return Message.ERROR_INSERT
    }
}

/**
 * Служебная функция.
 *
 * Удаляет значение по заданному ключу.
 */
fun delete(database: Database, key: String): Message {
    try {
        val mark = database.data[key]
        if (mark == null)
            return Message.MISSING_KEY
        else if (!mark.use)
            return Message.REMOTE_KEY
        else {
            mark.use = false
            database.counter++
            return Message.SUCCESSFUL_TRANSACTION
        }
    } catch (e: Exception) {
        return Message.ERROR_DELETE
    }
}

/**
 * Служебная функция.
 *
 * Ищет значение по ключу.
 */
fun find(database: Database, key: String): Pair<String, Message> {
    try {
        val mark = database.data[key]
        if (mark == null)
            return Pair(report(Message.MISSING_KEY), Message.MISSING_KEY)
        else if (!mark.use)
            return (Pair(report(Message.REMOTE_KEY), Message.REMOTE_KEY))
        else
            return Pair(mark.data + "\n", Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(report(Message.ERROR_FIND), Message.ERROR_FIND)
    }
}

/**
 * Служебная функция.
 *
 * Очищает базу данных от удаленных значений, перестраивает структуру данных.
 */
fun clear(database: Database): Pair<Database, Message> {
    try {
        return Pair(Database(database.name,
            database.data.filter { it.value.use } as HashMap<String, Mark>,
            0,
            database.dataFile,
            database.logFile), Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(database, Message.ERROR_CLEAR)
    }
}

/**
 * Служебная функция.
 *
 * Делает все записи активными.
 */
fun recovery(database: Database): Pair<Database, Message> {
    try {
        for (mark in database.data)
            mark.value.use = true
        database.counter = 0
        return Pair(database, Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(database, Message.ERROR_CLEAR)
    }
}

/**
 * Служебная функция.
 *
 * Логгирует операции, исполненные на базе данных.
 */
fun log(database: Database, line: String): Message {
    try {
        database.logFile.appendText(line + "\n")
        return Message.SUCCESSFUL_TRANSACTION
    } catch (e: Exception) {
        return Message.ERROR_LOG
    }
}

/**
 * Служебная функция.
 *
 * Загружает базу данных и лог файл.
 */
fun download(dataFile: File, logFile: File): Pair<Database, Message> {
    try {
        val json = dataFile.readText()
        val buffer = Json.decodeFromString<HashMap<String, Mark>>(json)
        logFile.writeText("")
        return Pair(Database("demo", buffer, 0, dataFile, logFile), Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(Database("", hashMapOf(), 0, dataFile, logFile), Message.ERROR_DOWNLOAD)
    }
}

/**
 * Служебная функция.
 *
 * Сохраняет базу данных из оперативной памяти на диск.
 */
fun save(database: Database): Pair<Database, Message> {
    try {
        val buffer = clear(database)
        if (buffer.second == Message.SUCCESSFUL_TRANSACTION) {
            val json = Json.encodeToString(buffer.first.data)
            database.dataFile.writeText(json)
            return Pair(buffer.first, Message.SUCCESSFUL_TRANSACTION)
        } else
            return Pair(buffer.first, Message.ERROR_CLEAR)
    } catch (e: Exception) {
        return Pair(database, Message.ERROR_SAVE)
    }
}

/**
 * Служебная функция.
 *
 * Сохраняет базу данных из оперативной памяти на диск.
 */
fun exit(pool: Pool, key: String): Message {
    try {
        val database = pool.data[key]
        if (database == null)
            return Message.MISSING_KEY
        else {
            val buffer = save(database)
            if (buffer.second == Message.SUCCESSFUL_TRANSACTION) {
                pool.data.remove(key)
                return Message.SUCCESSFUL_TRANSACTION
            } else
                return Message.ERROR_SAVE
        }
    } catch (e: Exception) {
        return Message.ERROR_EXIT
    }
}