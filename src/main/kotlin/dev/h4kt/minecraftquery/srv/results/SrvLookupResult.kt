package dev.h4kt.minecraftquery.srv.results

sealed class SrvLookupResult {

    data object Failure : SrvLookupResult()
    data object Empty : SrvLookupResult()

    data class Success(
        val host: String,
        val port: UShort
    ) : SrvLookupResult()

}
