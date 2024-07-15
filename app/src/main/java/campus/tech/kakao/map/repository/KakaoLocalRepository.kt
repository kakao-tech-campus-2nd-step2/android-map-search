package campus.tech.kakao.map.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.ResultSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class KakaoLocalRepository {


    suspend fun getPlaceData(text: String) : List<Place>? {
        Log.d("inputField", "inputText : ${text} ")
        var placeList = listOf<Place>()
        val retrofitService = Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val kakaoApi : KakaoLocalApi = retrofitService.create()

        val data = kakaoApi.getPlaceData(BuildConfig.KAKAO_LOCAL_API_KEY, text)
        return data.body()?.documents
    }
}