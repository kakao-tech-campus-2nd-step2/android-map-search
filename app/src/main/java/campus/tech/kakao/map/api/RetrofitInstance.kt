package campus.tech.kakao.map.api

import android.os.Build
import com.kakao.vectormap.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
object RetrofitInstance {
    private const val BASE_URL = "https://dapi.kakao.com/"

    private val client = OkHttpClient.Builder().build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: KakaoLocalApiService by lazy {
        instance.create(KakaoLocalApiService::class.java)
    }
}