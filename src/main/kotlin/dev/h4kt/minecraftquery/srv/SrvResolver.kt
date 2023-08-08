package dev.h4kt.minecraftquery.srv

interface SrvResolver {

    suspend fun resolveSrvRecords(
        hostName: String,
        service: String,
        protocol: String
    ): SrvResolveResult

}
