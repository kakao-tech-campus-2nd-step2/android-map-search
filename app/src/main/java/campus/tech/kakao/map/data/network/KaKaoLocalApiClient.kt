package campus.tech.kakao.map.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KaKaoLocalApiClient {
    private const val BASE_URL = "https://dapi.kakao.com"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
