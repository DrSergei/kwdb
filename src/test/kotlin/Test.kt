// Стандартная библиотека.
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

//Собственные пакеты.
import backend.*
import frontend.*
import parser.*
import style.*

internal class TestParser {

    @Test
    fun testoperation() {
        assertEquals(11, Operation.values().size)
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
    }
}

internal class TestFrontend {

    @Test
    fun testcheckFile() {
        assertEquals(true, checkFile(File("demo.dat")))
        assertEquals(true, checkFile(File("demo.log")))
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
        assertEquals(handlerInsert(pool, database, listOf("1","1")) , report(Message.SUCCESSFUL_TRANSACTION))
        assertEquals(handlerInsert(pool, databaseNull, listOf("1","1")) , report(Message.SUCCESSFUL_TRANSACTION))
        assertEquals(handlerInsert(pool, databaseRoot, listOf("1","1")) , "demo:" + report(Message.SUCCESSFUL_TRANSACTION))
        assertEquals(handlerInsert(pool, databaseNull, listOf("1")) , report(Message.INVALID_ARGUMENTS))
        assertEquals(handlerInsert(pool, database, listOf()) , report(Message.INVALID_ARGUMENTS))
    }

    @Test
    fun testhandlerDelete() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 0, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(handlerDelete(pool, database, listOf("x")) , report(Message.SUCCESSFUL_TRANSACTION))
        assertEquals(handlerDelete(pool, databaseNull, listOf("1")) , report(Message.MISSING_KEY))
        assertEquals(handlerDelete(pool, databaseRoot, listOf("1")) , "demo:" + report(Message.MISSING_KEY))
        assertEquals(handlerDelete(pool, databaseNull, listOf("1", "1")) , report(Message.INVALID_ARGUMENTS))
        assertEquals(handlerDelete(pool, database, listOf()) , report(Message.INVALID_ARGUMENTS))
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
        assertEquals( report(Message.MISSING_KEY), handlerDelete(pool, databaseNull, listOf("1")))
        assertEquals("demo:" + report(Message.MISSING_KEY), handlerDelete(pool, databaseRoot, listOf("1")) )
        assertEquals( report(Message.INVALID_ARGUMENTS), handlerDelete(pool, databaseNull, listOf("1", "1")) ,)
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDelete(pool, database, listOf()))
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
        assertEquals("demo:" + report(Message.SUCCESSFUL_TRANSACTION), handlerClear(pool, databaseRoot, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerClear(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerClear(pool, databaseNull, listOf("1", "1")) ,)
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
        assertEquals("demo:" + report(Message.SUCCESSFUL_TRANSACTION), handlerRecovery(pool, databaseRoot, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerRecovery(pool, databaseNull, listOf()))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerRecovery(pool, databaseNull, listOf("1", "1")) ,)
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerRecovery(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerDownload() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File(""), File(""))
        pool.data["demo"] = database
        val databaseNull = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, database, listOf()))
        assertEquals(report(Message.SUCCESSFUL_TRANSACTION), handlerDownload(pool, databaseRoot, listOf("demo", "demo.dat", "demo.log")))
        assertEquals(report(Message.ERROR_DOWNLOAD),handlerDownload(pool, databaseRoot, listOf("demo", "demo", "demo.log")))
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerDownload(pool, databaseRoot, listOf("1", "1")) ,)
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
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerSave(pool, databaseNull, listOf("1", "1")) ,)
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
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerExit(pool, databaseNull, listOf("1", "1")) ,)
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerExit(pool, database, listOf("1")))
    }

    @Test
    fun testhandlerError() {
        assertEquals(report(Message.INVALID_ARGUMENTS), handlerError())
    }
}

internal class TestBackend {

    @Test
    fun testinsert() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, insert(database, "y","y"))
        assertEquals(true, database.data.contains("y"))
        assertEquals(false, database.data.containsValue(Mark("z",false)))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, insert(database, "z","z"))
        assertEquals(true, database.data.contains("z"))
        assertEquals(true, database.data.containsValue(Mark("z",true)))
    }

    @Test
    fun testdelete() {
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true)), Pair("y", Mark("y", true))), 1, File(""), File(""))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, delete(database, "y"))
        assertEquals(true, database.data.contains("y"))
        assertEquals(true, database.data.containsValue(Mark("y",false)))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, delete(database, "x"))
        assertEquals(true, database.data.contains("x"))
        assertEquals(false, database.data.containsValue(Mark("x",true)))
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
        var buffer = download("demo", File("demo.dat"), File("demo.log"))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, buffer.second)
        assertEquals("demo", buffer.first.name)
        assertEquals(0, buffer.first.counter)
    }

    @Test
    fun testsave() {
        var database = Database("demo", hashMapOf(Pair("x", Mark("x", false))), 1, File("demo.dat"), File("demo.log"))
        assertEquals(Message.SUCCESSFUL_TRANSACTION, save(database).second)
        assertEquals(0, save(database).first.counter)
        assertEquals(false, save(database).first.data.containsValue(Mark("x",false)))
        assertEquals(false, save(database).first.data.contains("x"))
        database = Database("null", hashMapOf(), 0, File(""), File(""))
        assertEquals(Message.ERROR_SAVE, save(database).second)
    }

    @Test
    fun testexit() {
        val pool = Pool(mutableMapOf())
        val databaseRoot = Database("pool", hashMapOf(), 0, File(""), File(""))
        pool.data["pool"] = databaseRoot
        val database = Database("demo", hashMapOf(Pair("x", Mark("x", true))), 1, File("demo.dat"), File("demo.log"))
        pool.data["demo"] = database
        assertEquals(Message.SUCCESSFUL_TRANSACTION, exit(pool,database.name))
        assertEquals(1, pool.data.size)
        assertEquals(false, pool.data.contains("demo"))
        assertEquals(Message.ERROR_SAVE, exit(pool, databaseRoot.name))
    }
}

internal class TestStyle {

    @Test
    fun testmessage() {
        assertEquals(17, Message.values().size)
    }

    @Test
    fun testreport() {
        assertEquals('\n', report(Message.ERROR_SAVE).last())
        assertEquals('\n', report(Message.ERROR_READ).last())
        assertEquals('\n', report(Message.ERROR_WRITE).last())
        assertEquals('\n', report(Message.ERROR_EXIT).last())
        assertEquals('\n', report(Message.ERROR_INSERT).last())
        assertEquals('\n', report(Message.ERROR_DOWNLOAD).last())
        assertEquals('\n', report(Message.ERROR_DELETE).last())
        assertEquals('\n', report(Message.ERROR_FIND).last())
        assertEquals('\n', report(Message.ERROR_INSERT).last())
        assertEquals('\n', report(Message.ERROR_CLEAR).last())
    }
}