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
data class Mark(val data: String, var use: Boolean)

/**
 * Сама база данных.
 *
 *  Класс для хранения данных.
 */
data class Database(val data: HashMap<String, Mark>, var counter: Int, val dataFile: File, val logFile: File)

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
fun insert(database: Database, key: String, value: String) {
    try {
        database.data.put(key, Mark(value, true))
    } catch (e: Exception) {
    }
}

/**
 * Служебная функция.
 *
 * Удаляет значение по заданному ключу.
 */
fun delete(database: Database, key: String): Boolean {
    try {
        val mark = database.data.get(key)
        if (mark == null)
            return false
        else if (mark.use == false)
            return false
        else {
            mark.use = false
            return true
        }
    } catch (e: Exception) {
        return false
    }
}

/**
 * Служебная функция.
 *
 * Ищет значение по ключу.
 */
fun find(database: Database, key: String): String {
    try {
        val mark = database.data.get(key)
        if (mark == null)
            return ""
        else if (mark.use == false)
            return ""
        else {
            return mark.data
        }
    } catch (e: Exception) {
        return ""
    }
}

/**
 * Служебная функция.
 *
 * Очищает базу данных от удаленных значений, перестраивает структуру данных.
 */
fun clear(database: Database): Database {
    try {
        return Database(database.data.filter { it.value.use } as HashMap<String, Mark>,
            0,
            database.dataFile,
            database.logFile)
    } catch (e: Exception) {
        return database
    }
}

/**
 * Служебная функция.
 *
 * Логгирует операции, исполненные на базе данных.
 */
fun log(database: Database, line: String): Boolean {
    try {
        database.logFile.appendText(line + "\n")
        return true
    } catch (e: Exception) {
        report(Message.ERROR_LOG)
        return false
    }
}

/**
 * Служебная функция.
 *
 * Загружает базу данных и лог файл.
 */
fun download(dataFile: File, logFile: File): Database {
    try {
        val json = dataFile.readText()
        val buffer = Json.decodeFromString<HashMap<String, Mark>>(json)
        logFile.writeText("")
        return Database(buffer, 0, dataFile, logFile)
    } catch (e: Exception) {
        return Database(hashMapOf(), 0, dataFile, logFile)
    }
}

/**
 * Служебная функция.
 *
 * Сохраняет базу данных из оперативной памяти на диск.
 */
fun save(database: Database): Database {
    try {
        val json = Json.encodeToString(clear(database).data)
        database.dataFile.writeText(json)
        return database
    } catch (e: Exception) {
        return database
    }
}

/**
 * Служебная функция.
 *
 * Сохраняет базу данных из оперативной памяти на диск.
 */
fun exit(pool: Pool, key: String): Boolean {
    try {
        pool.data.remove(key)
        return true
    } catch (e: Exception) {
        return false
    }
}