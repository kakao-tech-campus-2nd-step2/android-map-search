package campus.tech.kakao.map.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.ResultSearch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class KakaoLocalRepository {

    object KakaoRetrofitInstance {

        val kakaoLocalApi : KakaoLocalApi = getApiClient().create()


        private fun getApiClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.KAKAO_BASE_URL)
                .client(provideOkHttpClient(AppInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
                = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain) : okhttp3.Response = with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("Authorization", BuildConfig.KAKAO_LOCAL_API_KEY)
                    .build()
                proceed(newRequest)
            }
        }
    }

    suspend fun getPlaceData(text: String) : List<Place>? {

        val kakaoApi = KakaoRetrofitInstance.kakaoLocalApi

        val data = kakaoApi.getPlaceData(text)
        return data.body()?.documents
    }
}