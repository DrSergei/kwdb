package backend

// Стандартная библиотека.
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

// Собственные пакеты.

// Запись в базе данных.
@Serializable
data class Mark(val data: String, var use : Boolean)

// Сама база данных.
data class Database(val data : HashMap<String, Mark>, var counter : Int, val file : File)

// Типы операций.
enum class Operation {
    DELETE, INSERT, FIND
}

// Операции с данными.
fun transaction(database: Database, operation : Operation, vararg args : String) {
    when (operation) {
        Operation.DELETE -> {
            if (args.size == 1) {
                val mark = database.data[args[0]]
                if (mark == null)
                    println("Нет элемента с ключом ${args[0]}")
                else if (!mark.use)
                    println("Элемент с ключом ${args[0]} удален")
                else
                    mark.use = false
            } else
                println("Неверное число аргументов")
        }
        Operation.FIND -> {
            if (args.size == 1) {
                val mark = database.data[args[0]]
                if (mark == null)
                    println("Нет элемента с ключом ${args[0]}")
                else
                    println(mark.data)
            } else
                println("Неверное число аргументов")
        }
        Operation.INSERT -> {
            if (args.size == 2)
                database.data[args[0]] = Mark(args[1], true)
            else
                println("Неверное число аргументов")
        }
    }
}

// Удаление не используемых ключей(очистка).
fun clear(database: Database) : Database{
    return Database(database.data.filter { it.value.use } as HashMap<String, Mark>, 0, database.file)
}

// Загрузка базы данных в оперативную память.
fun download(file : File) : Database {
    val json = file.readText()
    val buffer = Json.decodeFromString<HashMap<String, Mark>>(json)
    return Database(buffer, 0, file)
}

// Сохранение результатов в файл, после очистки.
fun save(database : Database) {
    try {
        val json = Json.encodeToString(clear(database).data)
        database.file.writeText(json)
    } catch (e : Exception) {

    }
}