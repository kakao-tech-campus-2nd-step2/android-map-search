package campus.tech.kakao.map.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.ResultSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoLocalRepository {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK "
    }

    fun getPlaceData(text : String, _place : MutableLiveData<List<Place>>){
        var placeList = listOf<Place>()
        val retrofitService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalApi::class.java)

        retrofitService.getPlaceData(API_KEY, text).enqueue(object : Callback<ResultSearch> {
            override fun onResponse(
                call: Call<ResultSearch>,
                response: Response<ResultSearch>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("testt", "body : ${body?.documents}")
                    placeList = body?.documents ?: listOf<Place>()
                    Log.d("testt", "placeList : ${placeList} ")
                    _place.postValue(placeList)
                }
            }

            override fun onFailure(call: Call<ResultSearch>, t: Throwable) {
                Log.d("testt","error : $t")
            }
        })
    }


}