package dev.h4kt.minecraftquery.client

import dev.h4kt.minecraftquery.engines.QueryEngine
import dev.h4kt.minecraftquery.engines.udp.UDPQueryEngine
import dev.h4kt.minecraftquery.srv.MiniDnsSrvResolver
import dev.h4kt.minecraftquery.srv.SrvResolver

class QueryClientBuilder {

    var engine: QueryEngine = UDPQueryEngine()
    var srvResolver: SrvResolver = MiniDnsSrvResolver
    var resolveSrvRecords = true

    fun build() = QueryClient(
        engine = engine,
        srvResolver = srvResolver,
        resolveSrvRecords = resolveSrvRecords
    )

}

fun QueryClient(configure: QueryBuilderConfigure? = null): QueryClient {
    return QueryClientBuilder().apply(configure ?: {}).build()
}
