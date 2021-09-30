/**
 * Пакет для разбора аргументов командной строки.
 *
 * Реализует функциональность через библиотеку kotlinx-cli.
 */
package cli

// Импорт.
import frontend.*
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
 * Служебная функция.
 *
 * Обрабатывает аргументы командной строки и выбирает сценарий работы.
 */
fun prepareArgs(args: Array<String>) {
    try {
        val parser = ArgParser("kvdb")
        val input by parser.option(ArgType.String, shortName = "i", description = "Input file(txt file)")
        val mode by parser.option(ArgType.Choice<Mode>(), shortName = "m", description = "Operating mode").default(Mode.READ)
        parser.parse(args)
        if (input != null)
            inputFile(input.toString(), mode)
        else
            inputConsole(mode)
    } catch (e: Exception) {
        println(report(INVALID_ARGUMENTS))
    }
}