// Стандартная библиоткеа.

// Собственные пакеты.
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        //input()
        print(readLine()!!.split(":"))
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
