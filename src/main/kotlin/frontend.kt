/**
 * Пакет для обработки общения с пользователем.
 *
 * Поддерживает консольный ввод и базовые проверки.
 */
package frontend

// Импорт.
import backend.*
import cli.*
import parser.*
import style.*
import java.io.*

/**
 * Служебная функция.
 *
 * Проверяет файлы на существование, расширение и доступ на чтение и запись.
 */
fun checkFile(file: File): Boolean {
    if (!file.exists()) {
        println(report(Message.MISSING_KEY) + file.absolutePath)
        return false
    }
    if (file.extension != "dat" && file.extension != "log") {
        println(file.name + " " + report(Message.INVALID_EXTENSION))
        return false
    }
    if (!file.canRead()) {
        println(file.name + " " + report(Message.ERROR_READ))
        return false
    }
    if (!file.canWrite()) {
        println(file.name + " " + report(Message.ERROR_WRITE))
        return false
    }
    return true
}

/**
 * Служебная функция.
 *
 * Обработчик операции вставки.
 */
fun handlerInsert(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.size == 2 && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}:" + report(insert(it.value, args[0], args[1])))
            }
            return result.toString()
        }
        (args.size == 2) -> return report(insert(database, args[0], args[1]))
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции удаления.
 */
fun handlerDelete(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.size == 1 && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}:" + report(delete(it.value, args[0])))
            }
            return result.toString()
        }
        (args.size == 1) -> return report(delete(database, args[0]))
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции удаления.
 */
fun handlerDeleteAll(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.size == 1 && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}: " + report(deleteAll(it.value, args[0])))
            }
            return result.toString()
        }
        (args.size == 1) -> return report(deleteAll(database, args[0]))
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции поиска.
 */
fun handlerFind(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.size == 1 && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}: " + find(it.value, args[0]).first)
            }
            return result.toString()
        }
        (args.size == 1) -> return  find(database, args[0]).first
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик поиска паттерна.
 */
fun handlerFindAll(pool: Pool, database: Database, args: List<String>) : String {
    when {
        (args.size == 1 && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}: " + findAll(it.value, args[0]).first)
            }
            return result.toString()
        }
        (args.size == 1) -> return findAll(database, args[0]).first
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции очистки.
 */
fun handlerClear(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.isEmpty() && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool") {
                    val buffer = clear(it.value)
                    pool.data[it.value.name] = buffer.first
                    result.append("${it.value.name}: " + report(buffer.second))
                }
            }
            return result.toString()
        }
        (args.isEmpty()) -> {
            val buffer = clear(database)
            pool.data[database.name] = buffer.first
            return report(buffer.second)
        }
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции восстановления.
 */
fun handlerRecovery(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.isEmpty() && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool") {
                    val buffer = recovery(it.value)
                    pool.data[it.value.name] = buffer.first
                    result.append("${it.value.name}: " + report(buffer.second))
                }
            }
            return result.toString()
        }
        (args.isEmpty()) -> {
            val buffer = recovery(database)
            pool.data[database.name] = buffer.first
            return report(buffer.second)
        }
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

fun handlerPrint(pool : Pool, database: Database, args: List<String>) : String {
    when {
        (args.isEmpty() && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if(it.key != "pool")
                    result.append(it.key + "\n")
            }
            return result.toString().dropLast(1) + "\n"
        }
        (args.isEmpty()) -> {
            val result = StringBuilder()
            database.data.map { result.append(it.key + ":" + it.value.data + "\n") }
            return result.toString().dropLast(1) + "\n"
        }
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}
/**
 * Служебная функция.
 *
 * Обработчик операции запроса размера записи.
 */
fun handlerSize(pool : Pool, database: Database, args: List<String>) : String {
    when {
        (args.isEmpty() && database.name == "pool") -> return "${pool.data.size - 1}\n"
        (args.isEmpty()) -> return "+${database.data.size} -${database.counter} =${database.data.size - database.counter}\n"
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}


/**
 * Служебная функция.
 *
 * Обработчик операции загрузки базы данных.
 */
fun handlerDownload(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.size == 4 && database.name == "pool") -> {
            val dataFile = File(args[1])
            val logFile = File(args[2])
            if (!(checkFile(dataFile) && checkFile(logFile)))
                return report(Message.INVALID_ARGUMENTS)
            val buffer = download(args[0], dataFile, logFile, args[3])
            if (buffer.second == Message.SUCCESSFUL_TRANSACTION)
                pool.data[args[0]] = buffer.first
            return report(buffer.second)
        }
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции сохранения базы данных.
 */
fun handlerSave(pool: Pool, database: Database, args: List<String>): String {
    when {
        (args.isEmpty() && database.name == "pool") -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool") {
                    val buffer = save(it.value)
                    pool.data[it.value.name] = buffer.first
                    result.append("${it.value.name}:" + report(buffer.second))
                }
            }
            return result.toString()
        }
        (args.isEmpty()) -> {
            val buffer = save(database)
            pool.data[database.name] = buffer.first
            return report(buffer.second)
        }
        else -> return report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции выхода из базы данных или пула.
 */
fun handlerExit(pool: Pool, database: Database, args: List<String>): String {
    return when {
        (database.name == "pool" && args.isEmpty()) -> {
            val result = StringBuilder()
            pool.data.map {
                if (it.key != "pool")
                    result.append("${it.value.name}:" + report(save(it.value).second))
            }
            pool.data.clear()
            pool.data["pool"] = Database("pool", hashMapOf(), 0, File(""), File(""))
            result.toString()
        }
        (args.isEmpty()) -> report(exit(pool, database.name))
        else -> report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик операции создания базы данных.
 */
fun handlerCreate(pool: Pool, database: Database, args: List<String>) : String {
    return when {
        (args.size == 4 && database.name == "pool" && pool.data[args[0]] == null) -> {
            val buffer = create(args[0], args[1], args[2], args[3])
            if (buffer.second != Message.SUCCESSFUL_TRANSACTION)
                report(Message.ERROR_CREATE)
            pool.data[buffer.first.name] = buffer.first
            report(buffer.second)
        }
        else -> report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Обработчик ошибочного ввода.
 */
fun handlerError(pool: Pool, database: Database, args: List<String>): String { // лишние аргументы для совместимости
    return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик пустого ввода.
 */
fun handlerNull(pool: Pool, database: Database, args: List<String>): String { // лишние аргументы для совместимости
    return ""
}

/**
 * Таблица методов.
 *
 * Таблица для режима чтения.
 */
val handlersRead = mapOf(
    Operation.INSERT to ::handlerError,
    Operation.DELETE to ::handlerError,
    Operation.DELETE_ALL to ::handlerError,
    Operation.FIND to ::handlerFind,
    Operation.FIND_ALL to ::handlerFindAll,
    Operation.CLEAR to ::handlerError,
    Operation.RECOVERY to ::handlerError,
    Operation.SIZE to ::handlerSize,
    Operation.PRINT to ::handlerPrint,
    Operation.DOWNLOAD to ::handlerDownload,
    Operation.SAVE to ::handlerError,
    Operation.EXIT to ::handlerExit,
    Operation.CREATE to ::handlerCreate,
    Operation.ERROR to ::handlerError,
    Operation.NULL to ::handlerNull,
    Operation.END to ::handlerNull,
)

/**
 * Таблица методов.
 *
 * Таблица для режима чтения и записи.
 */
val handlersWrite = mapOf(
    Operation.INSERT to ::handlerInsert,
    Operation.DELETE to ::handlerDelete,
    Operation.DELETE_ALL to ::handlerDeleteAll,
    Operation.FIND to ::handlerFind,
    Operation.FIND_ALL to ::handlerFindAll,
    Operation.CLEAR to ::handlerClear,
    Operation.RECOVERY to ::handlerRecovery,
    Operation.SIZE to ::handlerSize,
    Operation.PRINT to ::handlerPrint,
    Operation.DOWNLOAD to ::handlerDownload,
    Operation.SAVE to ::handlerSave,
    Operation.EXIT to ::handlerExit,
    Operation.CREATE to ::handlerCreate,
    Operation.ERROR to ::handlerError,
    Operation.NULL to ::handlerNull,
    Operation.END to ::handlerNull,
)

/**
 * Служебная функция.
 *
 * Выбирает нужный обработчик для каждой операции с учетом режима работы.
 */
fun distributionInput(pool: Pool, arguments: Arguments, mode: Mode): String {
    if (arguments.operation == Operation.NULL)
        return ""
    val database = pool.data[arguments.name] ?: return report(Message.INVALID_ARGUMENTS)
    log(database, listOf(arguments.operation.name, arguments.arg).joinToString(":"))
    when (mode) {
        Mode.READ -> return handlersRead[arguments.operation]?.invoke(pool, database, arguments.arg) ?: report(Message.INVALID_ARGUMENTS)
        Mode.WRITE -> return handlersWrite[arguments.operation]?.invoke(pool, database, arguments.arg) ?: report(Message.INVALID_ARGUMENTS)
    }
}

/**
 * Служебная функция.
 *
 * Цикл общения с пользователем(обработка ввода и работа с базой данных).
 */
fun inputConsole(mode: Mode) {
    val pool = Pool(mutableMapOf())
    pool.data["pool"] = Database("pool", hashMapOf(), 0, File(""), File(""))
    var request: String? = ""
    while (request != null) {
        val arguments = parser(request.trim())
        if (arguments.operation == Operation.END)
            return
        print(distributionInput(pool, arguments, mode))
        request = readLine()
    }
}

/**
 * Служебная функция.
 *
 * Цикл общения с пользователем(обработка ввода и работа с базой данных).
 */
fun inputFile(name: String, mode : Mode) {
    val pool = Pool(mutableMapOf())
    pool.data["pool"] = Database("pool", hashMapOf(), 0, File(""), File(""))
    val file = File(name)
    if (file.exists() && file.canRead()) {
        file.readLines().forEach { request ->
            val arguments = parser(request.trim())
            if (arguments.operation == Operation.END)
                return
            print(distributionInput(pool, arguments, mode))
        }
    }
}