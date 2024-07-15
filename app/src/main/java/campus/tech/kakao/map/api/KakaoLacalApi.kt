package campus.tech.kakao.map.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun searchKeyword(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): KakaoSearchResponse
}

// 데이터 모델
data class KakaoSearchResponse(
    val documents: List<Document>,
    val meta: Meta
)

data class Document(
    val place_name: String,
    val address_name: String,
    val category_group_name: String
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)
