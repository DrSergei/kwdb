/**
 * Пакет для разбора аргументов командной строки.
 *
 * Реализует функциональность через библиотеку kotlinx-cli.
 */
package cli

// Импорт.
import kotlinx.cli.*
import style.*
import style.Message.*


/**
 * Типы работы базы данной.
 *
 * Кодируют два основных режим режима работы.
 */
enum class Mode {
    READ, // только запись
    WRITE // редактирование
}

/**
 * Аргументы командной строки.
 *
 * Входной файл(если пустой, то консоль) и режим работы.
 */
data class ArgsCLI(val input: String, val mode: Mode)

/**
 * Служебная функция.
 *
 * Обрабатывает аргументы командной строки и возвращает их.
 */
fun prepareArgs(args: Array<String>) : ArgsCLI {
    try {
        val parser = ArgParser("kvdb")
        val input by parser.option(ArgType.String, shortName = "i", description = "Input file(txt file)")
        val mode by parser.option(ArgType.Choice<Mode>(), shortName = "m", description = "Operating mode").default(Mode.READ)
        parser.parse(args)
        if (input == null)
            return ArgsCLI("", mode)
        else
            return ArgsCLI(input!!, mode) // есть проверка на null, безопасный вызов
    } catch (e: Exception) {
        println(report(INVALID_ARGUMENTS))
        throw e
    }
}