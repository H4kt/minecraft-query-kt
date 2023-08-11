package dev.h4kt.minecraftquery.client.results

import dev.h4kt.minecraftquery.types.BasicServerInfo
import java.lang.Exception

sealed class BasicInfoQueryResult {

    data object SrvLookupFailed : BasicInfoQueryResult()

    data class Failure(
        val exception: Exception
    ) : BasicInfoQueryResult()

    class Success(
        val response: BasicServerInfo
    ) : BasicInfoQueryResult()

}
