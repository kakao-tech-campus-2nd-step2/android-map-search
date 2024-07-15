package campus.tech.kakao.map

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository {

    private val retrofitLocalService: RetrofitLocalService = RetrofitInstance.retrofitLocalService

    fun searchPlace(
        categoryName: String,
        onSuccess: (List<PlaceDataModel>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val call = retrofitLocalService.searchPlaceByCategory("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", CategoryGroupCodes.getCodeByName(categoryName) ?: "")
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    val categoryList: MutableList<PlaceDataModel> = mutableListOf()
                    val places = response.body()?.documents
                    places?.let {
                        for (placeInfo in places) {
                            val place = PlaceDataModel(
                                name = placeInfo.placeName,
                                category = placeInfo.categoryGroupName,
                                address = placeInfo.addressName
                            )
                            categoryList.add(place)
                        }
                    }
                    onSuccess(categoryList)
                    Log.d("API response", "Success: $places")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("API response", "Error response: $errorBody")
                }
            }

            override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
                Log.w("API response", "Failure: $throwable")
                onFailure(throwable)
            }
        })
    }
}
