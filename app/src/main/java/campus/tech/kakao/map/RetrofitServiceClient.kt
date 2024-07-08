package campus.tech.kakao.map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceClient {
    private lateinit var retrofit: Retrofit

    fun getRetrofit(baseUrl: String): Retrofit {
        if (!::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}