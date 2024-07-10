package campus.tech.kakao.map.network

import campus.tech.kakao.map.dto.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        @Query("query") query: String
    ): Call<ResultSearchKeyword>
}