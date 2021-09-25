/**
 * Пакет для обработки общения с пользователем.
 *
 * Поддерживает консольный ввод и базовые проверки.
 */
package frontend

// Стандартная библиотека.
import java.io.File

// Собственные пакеты.
import backend.*
import parser.*
import style.*

/**
 * Служебная функция.
 *
 * Проверяет файлы на существование, расширение и доступ на чтение и запись.
 */
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
    if (!file.canWrite()) {
        println("${file.name} не может быть записан")
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
    if (args.size == 2 && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool")
                result.append("${it.value.name}:" + report(insert(it.value, args[0], args[1])))
        }
        return result.toString()
    } else if (args.size == 2)
        return report(insert(database, args[0], args[1]))
    else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции удаления.
 */
fun handlerDelete(pool: Pool, database: Database, args: List<String>): String {
    if (args.size == 1 && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool")
                result.append("${it.value.name}:" + report(delete(it.value, args[0])))
        }
        return result.toString()
    } else if (args.size == 1)
        return report(delete(database, args[0]))
    else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции поиска.
 */
fun handlerFind(pool: Pool, database: Database, args: List<String>): String {
    if (args.size == 1 && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool")
                result.append("${it.value.name}:" + find(it.value, args[0]).first)
        }
        return result.toString()
    } else if (args.size == 1)
        return find(database, args[0]).first
    else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции очистки.
 */
fun handlerClear(pool: Pool, database: Database, args: List<String>): String {
    if (args.isEmpty() && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool") {
                val buffer = clear(it.value)
                pool.data[it.value.name] = buffer.first
                result.append("${it.value.name}:" + report(buffer.second))
            }
        }
        return result.toString()
    } else if (args.isEmpty()) {
        val buffer = clear(database)
        pool.data[database.name] = buffer.first
        return report(buffer.second)
    } else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции восстановления.
 */
fun handlerRecovery(pool: Pool, database: Database, args: List<String>): String {
    if (args.isEmpty() && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if(it.key != "pool") {
                val buffer = recovery(it.value)
                pool.data[it.value.name] = buffer.first
                result.append("${it.value.name}:" + report(buffer.second))
            }
        }
        return result.toString()
    } else if (args.isEmpty()) {
        val buffer = recovery(database)
        pool.data[database.name] = buffer.first
        return report(buffer.second)
    } else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции загрузки базы данных.
 */
fun handlerDownload(pool: Pool, database: Database, args: List<String>): String {
    if (args.size == 3 && database.name == "pool") {
        val dataFile = File(args[1])
        val logFile = File(args[2])
        val buffer = download(args[0], dataFile, logFile)
        pool.data[args[0]] = buffer.first
        return report(buffer.second)
    } else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции сохранения базы данных.
 */
fun handlerSave(pool: Pool, database: Database, args: List<String>): String {
    if (args.isEmpty() && database.name == "pool") {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool") {
                val buffer = save(it.value)
                pool.data[it.value.name] = buffer.first
                result.append("${it.value.name}:" + report(buffer.second))
            }
        }
        return result.toString()
    } else if (args.isEmpty()) {
        val buffer = save(database)
        pool.data[database.name] = buffer.first
        return report(buffer.second)
    } else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик операции выхода из базы данных или пула.
 */
fun handlerExit(pool: Pool, database: Database, args: List<String>): String {
    if (database.name == "pool" && args.isEmpty()) {
        val result = StringBuilder()
        pool.data.map {
            if (it.key != "pool")
                result.append("${it.value.name}:" + report(exit(pool, it.key)))
        }
        return result.toString()
    } else if (args.isEmpty())
        return report(exit(pool, database.name))
    else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Обработчик ошибочного ввода.
 */
fun handlerError(): String {
    return report(Message.INVALID_ARGUMENTS)
}

fun distributionInput(pool: Pool, arguments: Arguments): String {
    val database = pool.data[arguments.name]
    if (arguments.operation == Operation.NULL)
        return ""
    if (database != null) {
        log(database, listOf(arguments.operation.name, arguments.arg).joinToString(":"))
        when (arguments.operation) {
            Operation.INSERT -> return handlerInsert(pool, database, arguments.arg)
            Operation.DELETE -> return handlerDelete(pool, database, arguments.arg)
            Operation.FIND -> return handlerFind(pool, database, arguments.arg)
            Operation.CLEAR -> return handlerClear(pool, database, arguments.arg)
            Operation.RECOVERY -> return handlerRecovery(pool, database, arguments.arg)
            Operation.DOWNLOAD -> return handlerDownload(pool, database, arguments.arg)
            Operation.SAVE -> return handlerSave(pool, database, arguments.arg)
            Operation.EXIT -> return handlerExit(pool, database, arguments.arg)
            Operation.ERROR -> return handlerError()
            Operation.NULL -> return ""
            Operation.END -> return ""
        }
    } else
        return report(Message.INVALID_ARGUMENTS)
}

/**
 * Служебная функция.
 *
 * Цикл общения с пользователем(обработка ввода и работа с базой данных).
 */
fun input() {
    val pool = Pool(mutableMapOf())
    pool.data["pool"] = Database("pool", hashMapOf(), 0, File(""), File(""))
    pool.data["demo"] = download("demo", File("demo.dat"), File("demo.log")).first
    var request: String? = ""
    while (request != null) {
        val arguments = parser(request)
        if (arguments.operation == Operation.END)
            return
        print(distributionInput(pool, arguments))
        request = readLine()
    }
}