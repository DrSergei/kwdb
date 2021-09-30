// Импорт.

import cli.*
import kotlin.system.*

fun main(args: Array<String>) {
    try {
        val time = measureTimeMillis { // замер времени работы программы
            prepareArgs(args)
        }
        println("The session was ${time / 1000 /60} min.")
    } catch (e : Exception) {
        println("An error has occurred.")
    }
}