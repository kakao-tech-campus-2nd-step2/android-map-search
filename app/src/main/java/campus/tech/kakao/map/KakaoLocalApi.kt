package campus.tech.kakao.map

import android.app.appsearch.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("x") x: String? = null,
        @Query("y") y: String? = null
    ): Call<SearchResult>
}