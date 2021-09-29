// Импорт.
import cli.*
import kotlin.system.*

fun main(args: Array<String>) {
    val time = measureTimeMillis { // замер времени работы программы
        prepareArgs(args)
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
