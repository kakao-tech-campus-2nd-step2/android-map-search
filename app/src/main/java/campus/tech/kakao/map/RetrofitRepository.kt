package campus.tech.kakao.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.RetrofitInstance.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRepository {
    fun getPlace(query: String): List<Document> {
        var locationListLiveData: List<Document> = emptyList()

        if (query.isEmpty()) {
            locationListLiveData = emptyList()
        } else {
            retrofitService.getPlaces(
                "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY,
                query
            ).enqueue(object :
                Callback<Location> {
                override fun onResponse(
                    call: Call<Location>,
                    response: Response<Location>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            locationListLiveData = body.documents
                            Log.d("성공", "" + body.documents)
                        } else {
                            locationListLiveData = emptyList()
                        }
                    } else {
                        Log.d("태그", response.code().toString())
                        locationListLiveData = emptyList()
                    }
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("error", "" + t)
                    locationListLiveData = emptyList()
                }
            })
        }
        return locationListLiveData
    }
}