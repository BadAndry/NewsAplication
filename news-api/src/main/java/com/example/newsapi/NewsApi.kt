package com.example.newsapi

import androidx.annotation.IntRange
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.LanguagesDTO
import com.example.newsapi.models.ResponseDTO
import com.example.newsapi.models.SortByDTO
import com.example.newsapi.utils.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date


interface NewsApi {

    /**
     * [Api] https://newsapi.org/docs/endpoints/everything
     */

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<LanguagesDTO>? = null,
        @Query("sortBy") sortBy: SortByDTO? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>
}

fun ApiNews(
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    apiKeyInterceptor: String,
    json: Json = Json,
): NewsApi{
    val retrofit = retrofit(baseUrl, okHttpClient, apiKeyInterceptor, json)
    return retrofit.create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient?,
    apiKeyInterceptor: String,
    json: Json,
): Retrofit {

    /**
     * Динамическое добавление ApiKey через ApiInterceptor к каждому запросу.
     */
    val modifokHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(ApiKeyInterceptor(apiKeyInterceptor))
        .build()

    val jsonConvector = json.asConverterFactory(MediaType.get("application/json"))
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConvector)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifokHttpClient)
        .build()
}
