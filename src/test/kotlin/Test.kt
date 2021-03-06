// Модульные тесты ко всем файлам.

// Импорт.
import backend.*
import crypto.*
import frontend.*
import parser.*
import style.*
import java.io.*
import kotlin.test.*

internal class TestCrypto {

    @Test
    fun testcode() {
        assertEquals("hihihi", decode("abc",
            encode("abc", "hihihi".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("kotlin", decode("abc123",
            encode("abc123", "kotlin".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("java", decode("abcdef",
            encode("abcdef", "java".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("c++", decode("0000",
            encode("0000", "c++".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("jetbrains", decode("26344362",
            encode("26344362", "jetbrains".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("google", decode("xxx",
            encode("xxx", "google".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("spbu", decode("123456",
            encode("123456", "spbu".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("my name is sergei", decode("111",
            encode("111", "my name is sergei".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("1234567890", decode("egsk",
            encode("egsk", "1234567890".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
        assertEquals("20/20 points", decode("fff",
            encode("fff", "20/20 points".toByteArray(charset("utf-8")))).toString(charset("utf-8")))
    }
}

internal class TestParser {

    @Test
    fun testoperation() {
        assertEquals(16, Operation.values().size)
    }

    @Test
    fun testparser() {
        assertEquals(Arguments("", Operation.NULL, listOf()), parser(""))
        assertEquals(Arguments("demo", Operation.INSERT, listOf("1", "1")), parser("demo:insert:1:1"))
        assertEquals(Arguments("", Operation.ERROR, listOf()), parser("a:aaa"))
        assertEquals(Arguments("a", Operation.DELETE, listOf("key")), parser("a:delete:key"))
        assertEquals(Arguments("b", Operation.FIND, listOf("s")), parser("b:find:s"))
        assertEquals(Arguments("c", Operation.RECOVERY, listOf()), parser("c:recovery"))
        assertEquals(Arguments("d", Operation.CLEAR, listOf()), parser("d:clear"))
        assertEquals(Arguments("pool", Operation.DOWNLOAD, listOf()), parser("pool:download"))
        assertEquals(Arguments("f", Operation.SAVE, listOf()), parser("f:save"))
        assertEquals(Arguments("g", Operation.EXIT, listOf()), parser("g:exit"))
        assertEquals(Arguments("", Operation.END, listOf()), parser("end"))
        assertEquals(Arguments("g", Operation.SIZE, listOf()), parser("g:size"))
        assertEquals(Arguments("j", Operation.FIND_ALL, listOf("[0-9]")), parser("j:findAll:[0-9]"))
        assertEquals(Arguments("j", Operation.DELETE_ALL, listOf("[0-9]")), parser("j:deleteAll:[0-9]"))
        assertEquals(Arguments("l", Operation.PRINT, listOf()), parser("l:print"))
        assertEquals(Arguments("pool", Operation.CREATE, listOf("x", "x.dat", "x.log")), parser("pool:create:x:x.dat:x.log"))
    }
}

internal class TestFrontend {

    @Test
    fun testcheckFile() {
        assertEquals(true, checkFile(File("database\\demo\\demo.dat")))
        assertEquals(true, checkFile(File("database\\demo\\demo.log")))
        assertEquals(false, checkFile(File("demo")))
        assertEquals(false, checkFile(File("xxx.dat")))
        assertEquals(false, checkFile(File("123")))
    }

    @Test
    fun testhandlerInsert() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerInsert(pool, database, listOf("1", "1")))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerInsert(pool, databaseNull, listOf("1", "1")))
        assertEquals("demo:" + report(Message.SUCCESSFUL_TRANSACTION), handlerInsert(pool, databaseRoot, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerInsert(pool, databaseNull, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerInsert(pool, database, listOf()))
    }

    @Test
    fun testhandlerDelete() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerDelete(pool, database, listOf("x")))
        assertEquals(report(Message.MISSING_KEY), handlerDelete(pool, databaseNull, listOf("1")))
        assertEquals("demo:" + report(Message.MISSING_KEY), handlerDelete(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDelete(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDelete(pool, database, listOf()))
    }

    @Test
    fun testhandlerDeleteAll() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerDeleteAll(pool, database, listOf("x")))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerDeleteAll(pool, databaseNull, listOf("1")))
        assertEquals("demo: " + report(Message.SUCCESSFUL_TRANSACTION), handlerDeleteAll(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDeleteAll(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDeleteAll(pool, database, listOf()))
    }

    @Test
    fun testhandlerFind() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals("x\n", handlerFind(pool, database, listOf("x")))
        assertEquals(report(Message.MISSING_KEY), handlerFind(pool, databaseNull, listOf("1")))
        assertEquals("demo: " + report(Message.MISSING_KEY), handlerFind(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerFind(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerFind(pool, database, listOf()))
    }

    @Test
    fun testhandlerFindAll() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals("x\n", handlerFindAll(pool, database, listOf("x")))
        assertEquals("\n", handlerFindAll(pool, databaseNull, listOf("1")))
        assertEquals("demo: " + "\n", handlerFindAll(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerFindAll(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerFindAll(pool, database, listOf()))
    }

    @Test
    fun testhandlerClear() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerClear(pool, database, listOf()))
        assertEquals("demo: " + report(Message.SUCCESSFUL_TRANSACTION), handlerClear(pool, databaseRoot, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerClear(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerClear(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerClear(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerRecovery() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerRecovery(pool, database, listOf()))
        assertEquals("demo: " + report(Message.SUCCESSFUL_TRANSACTION), handlerRecovery(pool, databaseRoot, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerRecovery(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerRecovery(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerRecovery(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerSize() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals("+1 -1 =0\n", handlerSize(pool, database, listOf()))
        assertEquals("1\n", handlerSize(pool, databaseRoot, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerSize(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerSize(pool, database, listOf("1", "1")))
        assertEquals("+0 -0 =0\n", handlerSize(pool, databaseNull, listOf()))
    }

    @Test
    fun testhandlerPrint() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals("x:x\n", handlerPrint(pool, database, listOf()))
        assertEquals("demo\n", handlerPrint(pool, databaseRoot, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerPrint(pool, databaseRoot, listOf("1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerPrint(pool, database, listOf("1", "1")))
        assertEquals("\n", handlerPrint(pool, databaseNull, listOf()))
    }

    @Test
    fun testhandlerDownload() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File("x.log"))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, database, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerDownload(pool, databaseRoot, listOf("demo", "database\\demo\\demo.dat", "database\\demo\\demo.log", "")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, databaseRoot, listOf("demo", "database\\demo\\demo", "database\\demo\\demo.log", "")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, databaseRoot, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerSave() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.ERROR_SAVE), handlerSave(pool, database, listOf()))
        assertEquals("demo:" + report(Message.ERROR_SAVE), handlerSave(pool, databaseRoot, listOf()))
        assertEquals(report(Message.ERROR_SAVE), handlerSave(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerSave(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerSave(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerExit() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.ERROR_SAVE), handlerExit(pool, database, listOf()))
        assertEquals("demo:" + report(Message.ERROR_SAVE), handlerExit(pool, databaseRoot, listOf()))
        assertEquals(report(Message.MISSING_KEY), handlerExit(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerExit(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerExit(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerCreate() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerCreate(pool, databaseRoot, listOf("demo", "database\\demo\\demo.dat", "database\\demo\\demo.log", "")))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerCreate(pool, databaseRoot, listOf("n", "database\\test\\n.dat", "database\\test\\n.log", "")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerCreate(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerCreate(pool, databaseNull, listOf("1", "1")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerCreate(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerError() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerError(pool, database, listOf()))
    }

    @Test
    fun testhandlerNull() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        assertEquals("", handlerNull(pool, database, listOf()))
    }
}

internal class TestBackend {

    @Test
    fun testinsert() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, insert(database, "y", "y"))
        assertEquals(true, database.data.contains("y"))
        assertEquals(false, database.data.containsValue(Mark("z", false)))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, insert(database, "z", "z"))
        assertEquals(true, database.data.contains("z"))
        assertEquals(true, database.data.containsValue(Mark("z", true)))
    }

    @Test
    fun testdelete() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true)), Pair("y", Mark("y", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, delete(database, "y"))
        assertEquals(true, database.data.contains("y"))
        assertEquals(true, database.data.containsValue(Mark("y", false)))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, delete(database, "x"))
        assertEquals(true, database.data.contains("x"))
        assertEquals(false, database.data.containsValue(Mark("x", true)))
    }

    @Test
    fun testfind() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false)), Pair("y", Mark("y", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, find(database, "y").second)
        assertEquals("y\n", find(database, "y").first)
        assertEquals(Message.MISSING_KEY, find(database, "z").second)
        assertEquals(report(Message.MISSING_KEY), find(database, "z").first)
        assertEquals(Message.REMOTE_KEY, find(database, "x").second)
        assertEquals(report(Message.REMOTE_KEY), find(database, "x").first)
    }

    @Test
    fun testregex() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false)), Pair("y", Mark("y", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, findAll(database, "y").second)
        assertEquals("y\n", findAll(database, "y").first)
        assertEquals("\n", findAll(database, "z").first)
        assertEquals(Message.SUCCESSFUL_TRANSACTION, findAll(database, "z").second)
        assertEquals("y\n", findAll(database, "[a-z]").first)
    }

    @Test
    fun testclear() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, clear(database).second)
        assertEquals(0, clear(database).first.counter)
        assertEquals(false, clear(database).first.data.contains("x"))
        assertEquals(false, clear(database).first.data.containsValue(Mark("x", false)))
        assertEquals(false, clear(database).first.data.containsValue(Mark("x", true)))
    }

    @Test
    fun testrecovery() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, recovery(database).second)
        assertEquals(0, recovery(database).first.counter)
        assertEquals(true, recovery(database).first.data.contains("x"))
        assertEquals(false, recovery(database).first.data.containsValue(Mark("x", false)))
        assertEquals(true, recovery(database).first.data.containsValue(Mark("x", true)))
    }

    @Test
    fun testdownload() {
        var buffer = download("demo", File("database\\demo\\demo.dat"), File("database\\demo\\demo.log"))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, buffer.second)
        assertEquals("demo", buffer.first.name)
        assertEquals(0, buffer.first.counter)
        buffer = download("demo", File("database\\demo\\demo"), File("database\\demo\\demo.log"))
        assertEquals(Message.ERROR_DOWNLOAD, buffer.second)
    }

    @Test
    fun testsave() {
        var database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File("database\\demo\\demo.dat"), File("database\\demo\\demo.log"))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, save(database).second)
        assertEquals(0, save(database).first.counter)
        assertEquals(false, save(database).first.data.containsValue(Mark("x", false)))
        assertEquals(false, save(database).first.data.contains("x"))
        database = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(Message.ERROR_SAVE, save(database).second)
    }

    @Test
    fun testexit() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 1, File("database\\demo\\demo.dat"), File("database\\demo\\demo.log"))
        pool.data["demo"] = database
        assertEquals(Message.SUCCESSFUL_TRANSACTION, exit(pool, database.name))
        assertEquals(1, pool.data.size)
        assertEquals(false, pool.data.contains("demo"))
        assertEquals(Message.ERROR_SAVE, exit(pool, databaseRoot.name))
    }

    @Test
    fun testcreate() {
        assertEquals(Message.SUCCESSFUL_TRANSACTION, create("x", "database\\test\\x.dat", "database\\test\\x.log").second)
        assertEquals(0, create("y", "database\\test\\y.dat", "database\\test\\y.log").first.counter)
        assertEquals(0, create("z", "database\\test\\z.dat", "database\\test\\z.log").first.data.size)
        assertEquals("w", create("w", "database\\test\\w.dat", "database\\test\\w.log").first.name)
        assertEquals(Message.ERROR_CREATE, create("x", "database\\test\\x.dat", "database\\test\\x.log").second)
    }
}

internal class TestStyle {

    @Test
    fun testmessage() {
        assertEquals(20, Message.values().size)
    }

    @Test
    fun testreport() {
        for (message in Message.values())
            assertEquals('\n', report(message).last())
    }
}