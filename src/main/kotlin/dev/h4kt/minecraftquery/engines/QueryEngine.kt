package dev.h4kt.minecraftquery.engines

import dev.h4kt.minecraftquery.types.BasicServerInfo
import dev.h4kt.minecraftquery.types.FullServerInfo

interface QueryEngine {

    suspend fun queryBasicInfo(
        host: String,
        port: UShort
    ): BasicServerInfo

    suspend fun queryFullInfo(
        host: String,
        port: UShort
    ): FullServerInfo

}
