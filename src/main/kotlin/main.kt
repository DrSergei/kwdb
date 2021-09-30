// Импорт.

import cli.*
import kotlin.system.*

fun main(args: Array<String>) {
    try {
        val time = measureTimeMillis { // замер времени работы программы
            prepareArgs(args)
        }
        println("Сеанс составил ${time / 1000 / 60} мин")
    } catch (e : Exception) {
        println("Произошла ошибка.")
    }
}