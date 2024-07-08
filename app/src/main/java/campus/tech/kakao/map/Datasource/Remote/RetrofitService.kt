package campus.tech.kakao.map.Datasource.Remote

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Datasource.Remote.Response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET(URL)
    fun requestProducts(
        @Header("authorization") auth: String = KEY,
        @Query("x") x : Double = 127.06283102249932,
        @Query("y") y : Double = 37.514322572335935,
        @Query("radius") radius : Int = 20000,
        @Query("query",encoded = true) query: String = "",
    ): Call<SearchResponse>

    companion object {
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "/v2/local/search/keyword.json"
    }
}