package campus.tech.kakao.map

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import java.util.AbstractQueue

class PlaceRepository(private val apiService: KakaoAPIRetrofitService) {
    suspend fun searchPlaces(query: String): List<Document> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getSearchKeyword(
                key = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                query = query
            ).execute()
            if(response.isSuccessful) {
                response.body()?.documents ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}