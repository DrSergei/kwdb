// Стандартная библиоткеа.
import kotlin.system.measureTimeMillis

// Собственные пакеты.
import frontend.*

fun main() {
    val time = measureTimeMillis {
        input()
        //print(readLine()!!.split(":"))
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
