package campus.tech.kakao.map

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("/v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String
    ): KakaoSearchResponse
}