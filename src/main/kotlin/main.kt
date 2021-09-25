// Стандартная библиоткеа.
import kotlin.system.measureTimeMillis

// Собственные пакеты.
import frontend.*

fun main(args: Array<String>) {
    val time = measureTimeMillis {
        input()
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
