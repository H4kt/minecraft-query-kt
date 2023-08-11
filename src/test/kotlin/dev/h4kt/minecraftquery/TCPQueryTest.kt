package dev.h4kt.minecraftquery

import dev.h4kt.minecraftquery.client.QueryClient
import dev.h4kt.minecraftquery.engines.tcp.TCPQueryEngine
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class TCPQueryTest {

    private val client = QueryClient {
        engine = TCPQueryEngine(
            protocolVersion = 763 // Minecraft 1.20.1 (https://wiki.vg/Protocol_version_numbers)
        )
    }

    @Test
    fun testBasicInfo(): Unit = runBlocking {
        val result = client.queryBasicInfo("hypixel.net", 25565U)
        println(result)
    }

    @Test
    fun testFullInfo(): Unit = runBlocking {
        val result = client.queryFullInfo("hypixel.net", 25565U)
        println(result)
    }

}
