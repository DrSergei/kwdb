/**
 * Пакет для реализации базы данных.
 *
 * Используется HashMap для поддержки быстрых операций с базой данных,
 * есть функции для загрузки и сохранения работы
 * (хранит данные в json).
 */
package backend

// Импорт.
import crypto.*
import kotlinx.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import style.*
import java.io.*


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
    val key: String = ""
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
fun deleteAll(database: Database, key: String): Message {
    try {
        val regex = Regex(key)
        database.data.map {
            if (regex.matches(it.key))
                it.value.use = false
        }
        return Message.SUCCESSFUL_TRANSACTION
    } catch (e: Exception) {
        return Message.ERROR_DELETE_ALL
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
 * Ищет значение по ключу.
 */
fun findAll(database: Database, key: String): Pair<String, Message> {
    try {
        val regex = Regex(key)
        val result = StringBuilder()
        for (mark in database.data.filter { regex.matches(it.key) && it.value.use })
            result.append(mark.value.data + "\n")
        return Pair(result.toString().dropLast(1) + "\n", Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(report(Message.ERROR_FIND_ALL), Message.ERROR_FIND_ALL)
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
        database.data.map { it.value.use = true }
        //for (mark in database.data)
        //mark.value.use = true
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
fun download(name: String, dataFile: File, logFile: File, key: String = ""): Pair<Database, Message> {
    try {
        val json = decode(key, dataFile.readBytes()).toString(charset("utf-8"))
        val buffer = Json.decodeFromString<HashMap<String, Mark>>(json)
        logFile.writeText("")
        return Pair(Database(name, buffer, 0, dataFile, logFile, key), Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(Database(name, hashMapOf(), 0, dataFile, logFile, key), Message.ERROR_DOWNLOAD)
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
            database.dataFile.writeBytes(encode(database.key, json.toByteArray(charset("utf-8"))))
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
 * Отключает базу данных от пула.
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

/**
 * Служебная функция.
 *
 * Создает базу данных.
 */
fun create(name: String, pathData: String, pathLog: String, key: String = ""): Pair<Database, Message> {
    try {
        val dataFile = File(pathData)
        val logFile = File(pathLog)
        if (!dataFile.createNewFile() || !logFile.createNewFile())
            return Pair(Database(name, hashMapOf(), 0, File(""), File("")), Message.ERROR_CREATE)
        return Pair(Database(name, hashMapOf(), 0, dataFile, logFile, key), Message.SUCCESSFUL_TRANSACTION)
    } catch (e: Exception) {
        return Pair(Database(name, hashMapOf(), 0, File(""), File(""), key), Message.ERROR_CREATE)
    }
}