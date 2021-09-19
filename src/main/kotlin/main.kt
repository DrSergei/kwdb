// Стандартная библиоткеа.

// Собственные пакеты.
import frontend.input
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        input()
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
