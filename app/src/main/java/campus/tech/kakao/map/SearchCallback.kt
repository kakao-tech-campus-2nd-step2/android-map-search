package campus.tech.kakao.map

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCallback : Callback<SearchResult> {
    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
        if (response.isSuccessful) {
            val result = response.body()
            result?.documents?.forEach { place ->
                // 여기에 place 데이터 처리 로직
                Log.d("SearchCallback", "Place: ${place.place_name}")
            }
            Log.d("SearchCallback", "Data received from Kakao API")
        } else {
            Log.e("SearchCallback", "Error: ${response.code()}")
        }
    }

    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
        Log.e("SearchCallback", "Error: ${t.message}")
    }
}