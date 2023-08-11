package dev.h4kt.minecraftquery.client.results

import dev.h4kt.minecraftquery.types.FullServerInfo
import java.lang.Exception

sealed class FullInfoQueryResult {

    data object SrvLookupFailed : FullInfoQueryResult()

    data class Failure(
        val exception: Exception
    ) : FullInfoQueryResult()

    class Success(
        val response: FullServerInfo
    ) : FullInfoQueryResult()

}
