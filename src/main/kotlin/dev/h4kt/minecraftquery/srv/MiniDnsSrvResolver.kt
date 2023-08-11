package dev.h4kt.minecraftquery.srv

import dev.h4kt.minecraftquery.srv.results.SrvLookupResult
import io.ktor.utils.io.*
import org.minidns.dnslabel.DnsLabel
import org.minidns.dnsname.DnsName
import org.minidns.hla.DnssecResolverApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MiniDnsSrvResolver : SrvResolver {

    override suspend fun lookupSrvRecords(
        hostName: String,
        service: String,
        protocol: String
    ): SrvLookupResult = suspendCoroutine {

        val result = try {
            DnssecResolverApi.INSTANCE.resolveSrv(
                DnsLabel.from(service),
                DnsLabel.from(protocol),
                DnsName.from(hostName)
            )
        } catch (ex: Exception) {
            ex.printStack()
            it.resume(SrvLookupResult.Failure)
            return@suspendCoroutine
        }

        if (!result.wasSuccessful()) {
            it.resume(SrvLookupResult.Failure)
            return@suspendCoroutine
        }

        val record = result.sortedSrvResolvedAddresses
            .firstOrNull()
            ?: run {
                it.resume(SrvLookupResult.Empty)
                return@suspendCoroutine
            }

        val host = record.addresses
            .firstOrNull()
            ?.inetAddress
            ?.hostName
            ?: run {
                it.resume(SrvLookupResult.Empty)
                return@suspendCoroutine
            }

        it.resume(
            SrvLookupResult.Success(
                host = host,
                port = record.port.toUShort()
            )
        )
    }

}
