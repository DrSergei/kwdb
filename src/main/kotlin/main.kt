// Импорт.
import cli.*
import kotlin.system.*
//import javax.crypto.Cipher

fun main(args: Array<String>) {
    val time = measureTimeMillis { // замер времени работы программы
        prepareArgs(args)
    }
    println("Сеанс составил ${time / 1000 / 60} мин")
}
