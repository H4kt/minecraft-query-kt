package dev.h4kt.minecraftquery

import dev.h4kt.minecraftquery.engines.QueryEngine
import dev.h4kt.minecraftquery.engines.udp.UDPQueryEngine
import dev.h4kt.minecraftquery.srv.MiniDnsSrvResolver
import dev.h4kt.minecraftquery.srv.SrvResolveResult
import dev.h4kt.minecraftquery.srv.SrvResolver
import dev.h4kt.minecraftquery.types.BasicServerInfo
import dev.h4kt.minecraftquery.types.FullServerInfo

typealias QueryBuilderConfigure = QueryBuilder.() -> Unit

class Query(
    private val engine: QueryEngine,
    private val srvResolver: SrvResolver,
    private val resolveSrvRecords: Boolean
) {

    companion object {
        const val SRV_SERVICE = "_minecraft"
        const val SRV_PROTOCOL = "_tcp.mc"
    }

    suspend fun queryBasicInfo(
        host: String,
        port: UShort
    ): BasicServerInfo {

        val srvResolveResult = resolveSrvRecords(host, port)

        val (actualHost, actualPort) = when (srvResolveResult) {
            SrvResolveResult.Empty,
            SrvResolveResult.Failure ->
                host to port
            is SrvResolveResult.Success ->
                srvResolveResult.host to srvResolveResult.port
        }

        return engine.queryBasicInfo(
            host = actualHost,
            port = actualPort
        )
    }

    suspend fun queryFullInfo(
        host: String,
        port: UShort
    ): FullServerInfo {

        val srvResolveResult = resolveSrvRecords(host, port)

        val (actualHost, actualPort) = when (srvResolveResult) {
            SrvResolveResult.Empty,
            SrvResolveResult.Failure ->
                host to port
            is SrvResolveResult.Success ->
                srvResolveResult.host to srvResolveResult.port
        }

        return engine.queryFullInfo(
            host = actualHost,
            port = actualPort
        )
    }

    private suspend fun resolveSrvRecords(
        host: String,
        port: UShort
    ): SrvResolveResult = if (resolveSrvRecords) {
        srvResolver.resolveSrvRecords(
            hostName = host,
            service = SRV_SERVICE,
            protocol = SRV_PROTOCOL
        )
    } else {
        SrvResolveResult.Success(
            host = host,
            port = port
        )
    }

}

class QueryBuilder {

    var engine: QueryEngine = UDPQueryEngine()
    var srvResolver: SrvResolver = MiniDnsSrvResolver()
    var resolveSrvRecords = true

    fun build() = Query(
        engine = engine,
        srvResolver = srvResolver,
        resolveSrvRecords = resolveSrvRecords
    )

}

fun Query(configure: QueryBuilderConfigure? = null): Query {
    return QueryBuilder().apply(configure ?: {}).build()
}
