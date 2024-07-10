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

class KakaoLocalRepository {


    fun getPlaceData(text : String, callback: (List<Place>) -> Unit) {
        var placeList = listOf<Place>()
        val retrofitService = Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalApi::class.java)

        retrofitService.getPlaceData(BuildConfig.KAKAO_LOCAL_API_KEY, text).enqueue(object : Callback<ResultSearch> {
            override fun onResponse(
                call: Call<ResultSearch>,
                response: Response<ResultSearch>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("testt", "body : ${body?.documents}")
                    placeList = body?.documents ?: listOf<Place>()
                    Log.d("testt", "placeList : ${placeList} ")
                    callback(placeList)
                }
            }

            override fun onFailure(call: Call<ResultSearch>, t: Throwable) {
                Log.d("testt","error : $t")
                callback(placeList)
            }
        })
    }


}