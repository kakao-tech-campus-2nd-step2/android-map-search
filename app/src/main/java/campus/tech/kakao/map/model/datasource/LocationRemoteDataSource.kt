package campus.tech.kakao.map.model.datasource

import android.util.Log
import androidx.core.content.ContextCompat.getString
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.retrofit.KakaoAPI
import campus.tech.kakao.map.retrofit.RetrofitInstance
import com.android.identity.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRemoteDataSource {
    companion object{
        private const val RESULT_SIZE = 15
    }

    private val client = RetrofitInstance.getInstance().create(KakaoAPI::class.java)

    suspend fun getLocations(keyword: String): List<Location> {

        return withContext(Dispatchers.IO){
            val response = client.searchFromKeyword(keyword, RESULT_SIZE)
            val locationDtos: List<LocationDto> = response.body()?.documents ?: emptyList()
            Log.d("jieun", "locationDtos: " + locationDtos)
            toLocations(locationDtos)
        }
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            Location.toLocation(it)
        }
}