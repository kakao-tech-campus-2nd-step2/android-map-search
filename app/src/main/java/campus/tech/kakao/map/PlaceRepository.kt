package campus.tech.kakao.map

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository(private val apiService: KakaoAPIRetrofitService) {
    suspend fun searchPlaces(query: String): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSearchKeyword(
                    key = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                    query = query
                )
                response.documents ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}