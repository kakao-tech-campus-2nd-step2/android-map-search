package campus.tech.kakao.map.api

import campus.tech.kakao.map.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApiService {
    @GET("v2/local/search/keyword.json")
    fun searchPlaces(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): Call<SearchResponse>

}