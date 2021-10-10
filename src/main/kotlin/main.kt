// Импорт.

import cli.*
import frontend.*
import kotlin.system.*

fun main(args: Array<String>) {
    try {
        val time = measureTimeMillis { // замер времени работы программы
            val argsCLI = prepareArgs(args)
            if (argsCLI.input.isEmpty())
                inputConsole(argsCLI.mode)
            else
                inputFile(argsCLI.input, argsCLI.mode)
        }
        println("The session was ${time / 1000 /60} min.")
    } catch (e : Exception) {
        println("An error has occurred.")
    }
}