package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.KaKaoLocalApiClient
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.model.Place
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository {
    private val kakaoLocalService: KakaoLocalService =
        KaKaoLocalApiClient.retrofit.create(KakaoLocalService::class.java)

    private val categoryCodeMap =
        mapOf(
            "대형마트" to "MT1", "편의점" to "CS2", "어린이집" to "PS3",
            "유치원" to "PS3", "학교" to "SC4", "학원" to "AC5",
            "주차장" to "PK6", "주유소" to "OL7", "충전소" to "OL7",
            "지하철역" to "SW8", "은행" to "BK9", "문화시설" to "CT1",
            "중개업소" to "AG2", "공공기관" to "PO3", "관광명소" to "AT4",
            "숙박" to "AD5", "음식점" to "FD6", "카페" to "CE7",
            "병원" to "HP8", "약국" to "PM9",
        )

    fun getPlacesByCategory(
        categoryInput: String,
        callback: (List<Place>) -> Unit,
    ) {
        categoryCodeMap[categoryInput]?.let { categoryGroupCode ->
            fetchPlacesByCategory(categoryGroupCode, callback)
        }
    }

    private fun fetchPlacesByCategory(
        categoryGroupCode: String,
        callback: (List<Place>) -> Unit,
    ) {
        kakaoLocalService.getPlacesByCategory(categoryGroupCode)
            .enqueue(
                object : Callback<PlaceResponse> {
                    override fun onResponse(
                        call: Call<PlaceResponse>,
                        response: Response<PlaceResponse>,
                    ) {
                        if (response.isSuccessful) {
                            handleSuccessfulResponse(response, callback)
                        } else {
                            handleErrorResponse(response, callback)
                        }
                    }

                    override fun onFailure(
                        call: Call<PlaceResponse>,
                        t: Throwable,
                    ) {
                        handleFailure(t, callback)
                    }
                },
            )
    }

    private fun handleSuccessfulResponse(
        response: Response<PlaceResponse>,
        callback: (List<Place>) -> Unit,
    ) {
        val places =
            response.body()?.documents?.map { dto ->
                Place(
                    id = dto.placeId,
                    name = dto.placeName,
                    address = dto.address,
                    category = dto.category,
                )
            } ?: emptyList()
        callback(places)
    }

    private fun handleErrorResponse(
        response: Response<PlaceResponse>,
        callback: (List<Place>) -> Unit,
    ) {
        Log.e("PlaceRepository", "Failed to fetch places: ${response.errorBody()?.string()}")
        callback(emptyList())
    }

    private fun handleFailure(
        t: Throwable,
        callback: (List<Place>) -> Unit,
    ) {
        Log.e("PlaceRepository", "Failed to fetch places", t)
        callback(emptyList())
    }
}
