/**
 *  Пакет для шифрования.
 *
 *  Реализует шифр AES с построением полного ключа по пользовательскому.
 */
package crypto

// Импорт.
import javax.crypto.*
import javax.crypto.spec.*

/**
 * Служебная функция.
 *
 * Кодирует строку, полученным ключом.
 */
fun encode(key : String, message : ByteArray) : ByteArray {
    if (key == "")
        return message
    val buffer = key.repeat(16)
    val cipher = Cipher.getInstance("AES")
    val secretKey = SecretKeySpec(buffer.toByteArray().copyOf(16), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return cipher.doFinal(message)
}

/**
 * Служебная функция.
 *
 * Раскодирует строку, полученным ключом.
 */
fun decode(key : String, message : ByteArray) : ByteArray {
    if (key == "")
        return message
    val buffer = key.repeat(16)
    val cipher = Cipher.getInstance("AES")
    val secretKey = SecretKeySpec(buffer.toByteArray().copyOf(16), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return cipher.doFinal(message)
}