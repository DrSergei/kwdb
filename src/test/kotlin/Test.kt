
// Стандартная библиотека.
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

//Собственные пакеты.
import frontend.checkFile


internal class TestFrontend {

    @Test
    fun testcheckFile() {
        assertEquals(true, checkFile(File("demo.dat")))
        assertEquals(true, checkFile(File("demo.log")))
        assertEquals(false, checkFile(File("demo")))
        assertEquals(false, checkFile(File("xxx.dat")))
        assertEquals(false, checkFile(File("123")))
    }
}

internal class TestBackend {

    @Test
    fun test1() {
        assert(true)
    }
}

internal class TestParser {

    @Test
    fun test1() {
        assert(true)
    }
}
