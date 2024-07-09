package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.model.SearchResponse
import campus.tech.kakao.map.data.network.RetrofitObject
import campus.tech.kakao.map.data.network.RetrofitService
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepositoryImpl : PlaceRepository {
    private val retrofitService = RetrofitObject.retrofitService
    override fun searchPlaces(apiKey: String, query: String, callback: (List<Place>?) -> Unit) {
        retrofitService.searchKeyword(apiKey, query).enqueue(object : Callback<SearchResponse>  {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    println("body : $body")
                    val places = body?.documents?.map {
                        Place(
                            placeName = it.placeName,
                            addressName = it.addressName,
                            categoryName = it.categoryName
                        )
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                println("error : $t")
            }
        })
    }

}