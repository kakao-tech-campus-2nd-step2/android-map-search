package campus.tech.kakao.map.retrofit

import campus.tech.kakao.map.SearchFromKeywordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("/v2/local/search/keyword.json")
    suspend fun searchFromKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("size") size: Int
    ): Response<SearchFromKeywordResponse>
}