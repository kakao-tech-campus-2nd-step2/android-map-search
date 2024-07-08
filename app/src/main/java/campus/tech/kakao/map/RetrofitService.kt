package campus.tech.kakao.map

import campus.tech.kakao.map.json.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET(URL)
    fun requestPlaces(
        @Header("Authorization") auth: String = KEY,
        @Query("query") query: String = ""
    ): Call<SearchResponse>

    companion object {
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "/v2/local/search/keyword.json"
    }
}