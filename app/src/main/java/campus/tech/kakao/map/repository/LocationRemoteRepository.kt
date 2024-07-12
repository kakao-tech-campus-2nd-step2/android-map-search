package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.retrofit.KakaoAPI
import campus.tech.kakao.map.retrofit.RetrofitInstance

class LocationRemoteRepository {
    private val RESULT_SIZE = 15
    private val client = RetrofitInstance.getInstance().create(KakaoAPI::class.java)

    suspend fun getLocations(restApiKey:String, keyword: String): List<Location> {
        val response = client.searchFromKeyword(restApiKey, keyword, RESULT_SIZE)
        val locationDtos: List<LocationDto> = response.body()?.documents ?: emptyList()
        Log.d("jieun", "locationDtos: " + locationDtos)

        return toLocations(locationDtos)
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            Location.toLocation(it)
        }
}