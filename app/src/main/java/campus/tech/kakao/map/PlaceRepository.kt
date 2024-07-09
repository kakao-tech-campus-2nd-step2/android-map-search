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

/*
class PlaceRepository(private val dbHelper: DbHelper) {

    suspend fun insertInitialData() {
        withContext(Dispatchers.IO) {
            if(dbHelper.isDBEmpty()) {
                for(i in 1..10) {
                    dbHelper.insertData("카페 $i", "서울 성동구 성수동 $i", "카페")
                    dbHelper.insertData("약국 $i", "서울 강남구 대치동 $i", "약국")
                }
            }
        }
    }

    suspend fun searchDatabase(query: String): List<SearchResult> {
        return withContext(Dispatchers.IO) {
            dbHelper.searchDatabase(query)
        }
    }
}*/