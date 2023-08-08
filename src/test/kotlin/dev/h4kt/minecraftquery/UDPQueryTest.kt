package dev.h4kt.minecraftquery

import dev.h4kt.minecraftquery.engines.tcp.TCPQueryEngine
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class UDPQueryTest {

    val query = Query {
//        engine = TCPQueryEngine(
//            protocolVersion = 763
//        )
//        resolveSrvRecords = false
    }

    @Test
    fun testBasicInfo(): Unit = runBlocking {
        val result = query.queryBasicInfo("localhost", 25565U)
        println(result)
    }

    @Test
    fun testFullInfo(): Unit = runBlocking {
        val result = query.queryFullInfo("hypixel.net", 25565U)
        println(result)
    }

}
