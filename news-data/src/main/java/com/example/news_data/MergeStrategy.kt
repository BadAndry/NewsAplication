@file:Suppress("UNUSED_PARAMETER")
package com.example.news_data
import com.example.news_data.models.RequestResult

interface MergeStrategy<E> {

    fun merge(cache: E, server: E): E

}

internal class ResponseMergeStrategy<T: Any>: MergeStrategy<RequestResult<T>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(
        cache: RequestResult<T>,
        server: RequestResult<T>)
    : RequestResult<T> {
        return when {
            cache is RequestResult.InProgress && server is RequestResult.InProgress ->
                merge1(cache, server)
            cache is RequestResult.Success && server is RequestResult.InProgress ->
                merge2(cache, server)
            cache is RequestResult.InProgress && server is RequestResult.Success ->
                merge3(cache, server)
            cache is RequestResult.Success && server is RequestResult.Success ->
                merge4(cache, server)
            cache is RequestResult.Success && server is RequestResult.Error ->
                merge5(cache, server)
            cache is RequestResult.Error && server is RequestResult.Success ->
                merge6(cache, server)
            cache is RequestResult.Error && server is RequestResult.InProgress ->
                merge7(cache, server)
            cache is RequestResult.InProgress && server is RequestResult.Error ->
                merge8(cache, server)


            else -> error("Unimplemented branch right=$cache & left=$server")
        }
    }

    private fun merge1(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return if (server.data != null) {
            RequestResult.InProgress(server.data)
        } else RequestResult.InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge2(
        cache: RequestResult.Success<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge3(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(server.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge4(
        cache: RequestResult.Success<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(server.data)
    }

    private fun merge5(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = cache.data, error = server.error)
    }

    private fun merge6(
        cache: RequestResult.Error<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(server.data)
    }

    private fun merge7(
        cache: RequestResult.Error<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(server.data)
    }

    private fun merge8(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = server.data ?: cache.data, error = server.error)
    }
}

