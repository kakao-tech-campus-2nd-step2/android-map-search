package campus.tech.kakao.map.data.network.service

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.network.PlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalService {
    @Headers("Authorization: $KEY")
    @GET(URL)
    suspend fun getPlacesByCategory(
        @Query("category_group_code") categoryGroupCode: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<PlaceResponse>

    companion object {
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        const val URL = "/v2/local/search/category.json"
    }
}
