package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.KaKaoLocalApiClient
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override suspend fun getPlacesByCategory(categoryInput: String): List<Place> {
        val categoryGroupCode = categoryCodeMap[categoryInput] ?: return emptyList()
        return fetchPlacesByCategory(categoryGroupCode)
    }

    private suspend fun fetchPlacesByCategory(categoryGroupCode: String): List<Place> {
        val placeList = mutableListOf<Place>()
        val pageSize = 15
        val totalPageCount = 7

        coroutineScope {
            val deferredResults =
                (1..totalPageCount).map { page ->
                    async {
                        try {
                            kakaoLocalService.getPlacesByCategory(categoryGroupCode, page, pageSize)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

            deferredResults.forEach { deferred ->
                val response = deferred.await()
                response?.let {
                    if (it.isSuccessful) {
                        handleSuccessfulResponse(it, placeList)
                    } else {
                        handleErrorResponse(it)
                    }
                }
            }
        }

        return placeList
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

    private fun handleErrorResponse(response: Response<PlaceResponse>?) {
        Log.e("PlaceRepository", "Failed to fetch places: ${response?.errorBody()?.string()}")
    }
}
