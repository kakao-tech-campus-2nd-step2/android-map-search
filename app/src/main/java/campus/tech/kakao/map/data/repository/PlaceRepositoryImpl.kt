package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.KaKaoLocalApiClient
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
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

    override suspend fun getPlacesByCategory(
        categoryInput: String,
        totalPageCount: Int,
    ): List<Place> {
        val categoryGroupCode = categoryCodeMap[categoryInput] ?: return emptyList()
        return fetchPlacesByCategory(categoryGroupCode, totalPageCount)
    }

    private suspend fun fetchPlacesByCategory(
        categoryGroupCode: String,
        totalPageCount: Int,
    ): List<Place> {
        val placeList = mutableListOf<Place>()

        supervisorScope {
            val deferredResults =
                createDeferredResults(this, categoryGroupCode, totalPageCount)

            processDeferredResults(deferredResults, placeList)
        }

        return placeList
    }

    private fun createDeferredResults(
        scope: CoroutineScope,
        categoryGroupCode: String,
        totalPageCount: Int,
    ): List<Deferred<Response<PlaceResponse>?>> {
        return (1..totalPageCount).map { page ->
            scope.async {
                try {
                    kakaoLocalService.getPlacesByCategory(categoryGroupCode, page)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    private suspend fun processDeferredResults(
        deferredResults: List<Deferred<Response<PlaceResponse>?>>,
        placeList: MutableList<Place>,
    ) {
        deferredResults.forEach { deferred ->
            val response = deferred.await()
            if (response != null) {
                if (response.isSuccessful) {
                    handleSuccessfulResponse(response, placeList)
                } else {
                    handleErrorResponse(response)
                }
            } else {
                Log.e("CoroutineException", "Exception occurred in coroutine")
            }
        }
    }

    private fun handleSuccessfulResponse(
        response: Response<PlaceResponse>,
        allPlaces: MutableList<Place>,
    ) {
        response.body()?.documents?.mapTo(allPlaces) { dto ->
            Place(
                id = dto.placeId,
                name = dto.placeName,
                address = dto.address,
                category = dto.category,
            )
        }
    }

    private fun handleErrorResponse(response: Response<PlaceResponse>?) {
        Log.e("PlaceRepository", "Failed to fetch places: ${response?.errorBody()?.string()}")
    }
}
