package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.KaKaoLocalApiClient
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PlaceRepositoryImpl : PlaceRepository {
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

    override fun getPlacesByCategory(
        categoryInput: String,
        callback: (List<Place>) -> Unit,
    ) {
        categoryCodeMap[categoryInput]?.let { categoryGroupCode ->
            CoroutineScope(Dispatchers.IO).launch {
                fetchPlacesByCategory(categoryGroupCode, callback)
            }
        } ?: callback(emptyList())
    }

    private suspend fun fetchPlacesByCategory(
        categoryGroupCode: String,
        callback: (List<Place>) -> Unit,
    ) {
        val placeList = mutableListOf<Place>()
        val pageSize = 15
        val totalPageCount = 7

        for (page in 1..totalPageCount) {
            try {
                val response =
                    kakaoLocalService.getPlacesByCategory(categoryGroupCode, page, pageSize)

                if (response.isSuccessful) {
                    handleSuccessfulResponse(response, placeList)
                } else {
                    withContext(Dispatchers.Main) {
                        handleErrorResponse(response, callback)
                        return@withContext
                    }
                }
            } catch (e: Exception) {
                Log.e("PlaceRepository", "API call failed", e)
                withContext(Dispatchers.Main) {
                    callback(emptyList())
                    return@withContext
                }
            }
        }
        withContext(Dispatchers.Main) {
            callback(placeList)
        }
    }

    private fun handleSuccessfulResponse(
        response: Response<PlaceResponse>,
        allPlaces: MutableList<Place>,
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
        allPlaces.addAll(places)
    }

    private fun handleErrorResponse(
        response: Response<PlaceResponse>?,
        callback: (List<Place>) -> Unit,
    ) {
        Log.e("PlaceRepository", "Failed to fetch places: ${response?.errorBody()?.string()}")
        callback(emptyList())
    }
}
