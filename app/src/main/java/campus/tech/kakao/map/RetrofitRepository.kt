package campus.tech.kakao.map

import android.util.Log
import campus.tech.kakao.map.RetrofitInstance.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRepository {
    fun getPlace(query: String, callback: (List<Document>) -> Unit) {
        if (query.isEmpty()) {
            callback(emptyList())
            return
        }

        retrofitService.getPlaces(
            "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY,
            query
        ).enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    callback(body?.documents ?: emptyList())
                    Log.d("성공", "" + body?.documents)
                } else {
                    Log.d("태그", response.code().toString())
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.d("error", "" + t)
                callback(emptyList())
            }
        })
    }
}