package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData<List<MapItem>>()
    val searchResults: LiveData<List<MapItem>> get() = _searchResults

    fun searchPlaces(keyword: String) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        RetrofitClient.apiService.searchPlaces(apiKey, keyword).enqueue(object :
            Callback<KakaoMapProductResponse> {
            override fun onResponse(call: Call<KakaoMapProductResponse>, response: Response<KakaoMapProductResponse>) {
                if (response.isSuccessful) {
                    val documents = response.body()?.documents ?: emptyList()
                    val results = documents.map { MapItem(it.place_name, it.address_name, it.category_group_name) }
                    _searchResults.postValue(results)
                } else {
                    _searchResults.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<KakaoMapProductResponse>, t: Throwable) {
                _searchResults.postValue(emptyList())
            }
        })
    }
}

data class MapItem(
    val name: String,
    val address: String,
    val category: String
)
