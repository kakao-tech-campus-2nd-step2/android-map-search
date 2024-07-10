package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {

    @GET("v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("category_group_code") categoryGroupCode: String? = null,
        @Query("x") longitude: String? = null,
        @Query("y") latitude: String? = null,
        @Query("radius") radius: Int? = null
    ): Call<KakaoSearchResponse>
}