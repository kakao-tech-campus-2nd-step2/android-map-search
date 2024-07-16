package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoApiDataSource {

    suspend fun getPlaceData(text: String) : List<Place> {
        Log.d("inputField", "inputText : ${text} ")
        Log.d("coroutineTest", "getPlaceData")
        val emptyList = listOf<Place>()
        val retrofitService = Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL + "12")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val kakaoApi = retrofitService.create(KakaoLocalApi::class.java)
        
        return try{
            val placeList = kakaoApi.getPlaceData(BuildConfig.KAKAO_LOCAL_API_KEY, text)
            Log.d("coroutineTest", "return")
            placeList.documents ?: emptyList
        } catch (e : Exception){
            emptyList
        }
    }
}