/**
 *  Пакет для шифрования.
 *
 *  Реализует шифр Вернама с построением полного ключа по пользовательскому.
 */
package crypto

// Импорт.

/**
 * Служебная функция.
 *
 * Кодирует строку, полученным ключом.
 */
fun encode(key : String, message : String) : String {
    if (key == "")
        return message
    val result = StringBuilder()
    for (index in message.indices) {
        result.append((message[index].code xor key[index % key.length].code).toChar())
    }
    return result.toString()
}

/**
 * Служебная функция.
 *
 * Раскодирует строку, полученным ключом.
 */
fun decode(key : String, message : String) : String {
    if (key == "")
        return message
    val result = StringBuilder()
    for (index in message.indices) {
        result.append((message[index].code xor key[index % key.length].code).toChar())
    }
    return result.toString()
}