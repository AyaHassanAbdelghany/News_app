package com.example.news_app.network

import com.example.news_app.model.Keys
import com.example.news_app.pojo.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsService {

    @Headers(
        "apiKey: a1c2f37b0a744dcf9026ae1e1bcee545"
    )
    @GET(Keys.TOP_HEADLINES)
    suspend fun getNews(
         @Query("category") category:String = "general",
         @Query("country") country:String = "us",
        ): NewsResponse

}