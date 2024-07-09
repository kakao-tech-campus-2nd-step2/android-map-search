package campus.tech.kakao.map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoAPIRetrofitClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    val retrofitService: KakaoAPIRetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoAPIRetrofitService::class.java)
    }
}