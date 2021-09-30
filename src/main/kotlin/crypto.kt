/**
 *  Пакет для шифрования.
 *
 *  Реализует шифр Вернама с построением полного ключа по пользовательскому.
 */
package crypto

import kotlin.experimental.*
import kotlin.random.*

// Импорт.

/**
 * Служебная функция.
 *
 * Кодирует строку, полученным ключом.
 */
fun encode(key : String, message : ByteArray) : ByteArray {
    if (key == "")
        return message
    val result = ByteArray(message.size)
    val random = Random(key.sumOf { it.code }) // ключ строится генератором случайных чисел по xor символов пароля
    for (index in message.indices) {
        result[index] = message[index].xor(random.nextBytes(1).first())
    }
    return result
}

/**
 * Служебная функция.
 *
 * Раскодирует строку, полученным ключом.
 */
fun decode(key : String, message : ByteArray) : ByteArray {
    if (key == "")
        return message
    val result = ByteArray(message.size)
    val random = Random(key.sumOf { it.code }) // ключ строится генератором случайных чисел по xor символов пароля
    for (index in message.indices) {
        result[index] = message[index].xor(random.nextBytes(1).first())
    }
    return result
}