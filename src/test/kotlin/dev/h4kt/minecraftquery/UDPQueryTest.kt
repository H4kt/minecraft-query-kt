package dev.h4kt.minecraftquery

import dev.h4kt.minecraftquery.client.QueryClient
import dev.h4kt.minecraftquery.engines.udp.UDPQueryEngine
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class UDPQueryTest {

    private val client = QueryClient {
        engine = UDPQueryEngine(
            timeout = 5.seconds
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
