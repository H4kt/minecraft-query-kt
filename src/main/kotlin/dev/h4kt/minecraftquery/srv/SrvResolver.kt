package dev.h4kt.minecraftquery.srv

import dev.h4kt.minecraftquery.srv.results.SrvLookupResult

interface SrvResolver {

    suspend fun lookupSrvRecords(
        hostName: String,
        service: String,
        protocol: String
    ): SrvLookupResult

}
