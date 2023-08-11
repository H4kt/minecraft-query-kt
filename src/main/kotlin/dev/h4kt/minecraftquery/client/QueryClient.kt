package dev.h4kt.minecraftquery.client

import dev.h4kt.minecraftquery.client.results.BasicInfoQueryResult
import dev.h4kt.minecraftquery.client.results.FullInfoQueryResult
import dev.h4kt.minecraftquery.engines.QueryEngine
import dev.h4kt.minecraftquery.srv.results.SrvLookupResult
import dev.h4kt.minecraftquery.srv.SrvResolver

typealias QueryBuilderConfigure = QueryClientBuilder.() -> Unit

class QueryClient(
    private val engine: QueryEngine,
    private val srvResolver: SrvResolver,
    private val resolveSrvRecords: Boolean
) {

    companion object {
        const val SRV_SERVICE = "_minecraft"
        const val SRV_PROTOCOL = "_tcp"
    }

    suspend fun queryBasicInfo(
        host: String,
        port: UShort
    ): BasicInfoQueryResult {

        val srvResolveResult = lookupSrvRecord(host, port)

        val (actualHost, actualPort) = when (srvResolveResult) {
            SrvLookupResult.Failure ->
                return BasicInfoQueryResult.SrvLookupFailed
            SrvLookupResult.Empty ->
                host to port
            is SrvLookupResult.Success ->
                srvResolveResult.host to srvResolveResult.port
        }

        val response = try {
            engine.queryBasicInfo(
                host = actualHost,
                port = actualPort
            )
        } catch (ex: Exception) {
            return BasicInfoQueryResult.Failure(ex)
        }

        return BasicInfoQueryResult.Success(
            response = response
        )
    }

    suspend fun queryFullInfo(
        host: String,
        port: UShort
    ): FullInfoQueryResult {

        val srvResolveResult = lookupSrvRecord(host, port)

        val (actualHost, actualPort) = when (srvResolveResult) {
            SrvLookupResult.Failure ->
                return FullInfoQueryResult.SrvLookupFailed
            SrvLookupResult.Empty ->
                host to port
            is SrvLookupResult.Success ->
                srvResolveResult.host to srvResolveResult.port
        }

        val response = try {
            engine.queryFullInfo(
                host = actualHost,
                port = actualPort
            )
        } catch (ex: Exception) {
            return FullInfoQueryResult.Failure(ex)
        }

        return FullInfoQueryResult.Success(
            response = response
        )
    }

    private suspend fun lookupSrvRecord(
        host: String,
        port: UShort
    ): SrvLookupResult = if (resolveSrvRecords) {
        srvResolver.lookupSrvRecords(
            hostName = host,
            service = SRV_SERVICE,
            protocol = SRV_PROTOCOL
        )
    } else {
        SrvLookupResult.Success(
            host = host,
            port = port
        )
    }

}
