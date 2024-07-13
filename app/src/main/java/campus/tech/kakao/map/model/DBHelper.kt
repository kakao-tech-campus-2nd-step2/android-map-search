package campus.tech.kakao.map.model

import android.content.Context
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DBHelper(context: Context) {

    companion object {
        private const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
    }
    private val retrofit = RetrofitInstance.api


    fun searchPlaces(query: String, categoryGroupName: String, callback: (List<Document>) -> Unit) {
        retrofit.getPlaces(API_KEY, categoryGroupName, query).enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    val places = response.body()?.documents ?: emptyList()
                    callback(places)
                } else {
                    Log.e("DBHelper", "Failed to fetch places: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.e("DBHelper", "Failed to fetch places: ${t.message}")
                callback(emptyList())
            }
        })
    }
}
