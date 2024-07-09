package campus.tech.kakao.map

import com.google.gson.internal.GsonBuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("keyword")
    fun getPlaces(
        @Header("Authorization") authorization: String = "KakaoAK "+BuildConfig.KAKAO_REST_API_KEY ,
        @Query("query") query: String
    ): Call<Location>
}