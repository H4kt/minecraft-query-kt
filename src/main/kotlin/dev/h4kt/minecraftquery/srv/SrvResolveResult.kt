package dev.h4kt.minecraftquery.srv

sealed class SrvResolveResult {

    data object Failure : SrvResolveResult()
    data object Empty : SrvResolveResult()

    data class Success(
        val host: String,
        val port: UShort
    ) : SrvResolveResult()

}
