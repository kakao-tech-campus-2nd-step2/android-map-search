package campus.tech.kakao.map

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {

    @GET("/v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("size") size: Int = 15, // 검색 결과 수 (기본값: 15)
        @Query("page") page: Int = 1, // 페이지 번호 (기본값: 1)
        @Query("analyze_type") analyzeType: String = "similar"
    ): Documents
}