package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.MapView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Map_Activity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchView: SearchView
    private lateinit var kakaoApiService: KakaoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "e6a7c826ae7a55df129b8be2c636e213") // Kakao SDK 초기화
        setContentView(R.layout.activity_map)
        kakaoApiService = createKakaoApiService()

        mapView = findViewById(R.id.map_view)
        searchView = findViewById(R.id.search_text)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, Search_Activity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun createKakaoApiService(): KakaoApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK 13c6b4e1c003d0f42b3b07888391c355")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(KakaoApiService::class.java)
    }

}