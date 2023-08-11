package dev.h4kt.minecraftquery

import dev.h4kt.minecraftquery.srv.MiniDnsSrvResolver
import dev.h4kt.minecraftquery.srv.results.SrvLookupResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class MiniDnsSrvResolveTest {

    private val resolver = MiniDnsSrvResolver

    @Test
    fun test(): Unit = runBlocking {

        val result = resolver.lookupSrvRecords(
            hostName = "h4kt.dev",
            protocol = "_tcp",
            service = "_minecraft"
        )

        assert(result is SrvLookupResult.Success)

    }

}
