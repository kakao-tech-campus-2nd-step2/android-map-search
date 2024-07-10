package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitLocalService {
    @GET("/v2/local/search/keyword.json")
    fun requestPlace(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<SearchResult>
}