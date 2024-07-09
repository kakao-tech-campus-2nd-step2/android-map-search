package campus.tech.kakao.map.model

import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LocalSearchService {
    @GET("search/keyword.json")
    fun requestLocalSearch(
        @Header("Authorization") authorization: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): Call<LocalSearchResponse>
}