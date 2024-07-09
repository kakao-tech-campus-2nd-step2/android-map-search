package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("/v2/local/search/category.json")
    fun getSearchCategory(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryGroupCode: String,
//        @Query("x") x: String,
//        @Query("y") y: String,
//        @Query("radius") radius: Int,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("sort") sort: String = "accuracy"
    ): Call<KakaoResponse>
}
