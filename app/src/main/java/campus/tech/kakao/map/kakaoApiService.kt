package campus.tech.kakao.map

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {

    @GET("/v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("size") size: Int = 15,
        @Query("page") page: Int = 1,
        @Query("analyze_type") analyzeType: String = "similar"
    ): Documents
}