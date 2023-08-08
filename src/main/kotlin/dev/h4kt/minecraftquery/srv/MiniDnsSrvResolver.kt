package dev.h4kt.minecraftquery.srv

import io.ktor.utils.io.*
import org.minidns.dnslabel.DnsLabel
import org.minidns.dnsname.DnsName
import org.minidns.hla.DnssecResolverApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MiniDnsSrvResolver : SrvResolver {

    override suspend fun resolveSrvRecords(
        hostName: String,
        service: String,
        protocol: String
    ): SrvResolveResult = suspendCoroutine {

        val result = try {
            DnssecResolverApi.INSTANCE.resolveSrv(
                DnsLabel.from(service),
                DnsLabel.from(protocol),
                DnsName.from(hostName)
            )
        } catch (ex: Exception) {
            ex.printStack()
            it.resume(SrvResolveResult.Failure)
            return@suspendCoroutine
        }

        if (!result.wasSuccessful()) {
            it.resume(SrvResolveResult.Failure)
            return@suspendCoroutine
        }

        val record = result.sortedSrvResolvedAddresses
            .firstOrNull()
            ?: run {
                it.resume(SrvResolveResult.Empty)
                return@suspendCoroutine
            }

        val host = record.addresses
            .firstOrNull()
            ?.inetAddress
            ?.hostName
            ?: run {
                it.resume(SrvResolveResult.Empty)
                return@suspendCoroutine
            }

        it.resume(
            SrvResolveResult.Success(
                host = host,
                port = record.port.toUShort()
            )
        )
    }

}
