package campus.tech.kakao.map.data.repository

import android.content.Context
import android.util.Log
import campus.tech.kakao.map.data.PreferenceHelper
import campus.tech.kakao.map.data.model.SearchResponse
import campus.tech.kakao.map.data.network.RetrofitObject
import campus.tech.kakao.map.data.network.RetrofitService
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepositoryImpl(private val context: Context) : PlaceRepository {
    private val retrofitService = RetrofitObject.retrofitService
    override fun searchPlaces(apiKey: String, query: String, callback: (List<Place>?) -> Unit) {
        retrofitService.searchKeyword(apiKey, query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("testt", "Response body: $body")
                    val places = body?.documents?.map {
                        Log.d("testt", "Place: $it")
                        Place(
                            placeName = it.placeName,
                            addressName = it.addressName,
                            categoryName = it.categoryGroupName
                        )
                    }
                    callback(places)
                } else {
                    Log.d("testt", "Response failed: ${response.errorBody().toString()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("testt", "Error: ${t.message}")
                callback(null)
            }
        })
    }

    override fun saveSearchQuery(place: Place) {
        PreferenceHelper.saveSearchQuery(context, place.placeName)
    }

    override fun getSearchHistory(): Set<String> {
        return PreferenceHelper.getSearchHistory(context)
    }

    override fun removeSearchQuery(query: String) {
        PreferenceHelper.removeSearchQuery(context, query)
    }
}