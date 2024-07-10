package campus.tech.kakao.map

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepository {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com"
        const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
    }

    suspend fun Search(searchKeyword: SearchKeyword): List<Place> {
        return withContext(Dispatchers.IO) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KakaoSearchKeywordAPI::class.java)
                .getSearchKeyWord(API_KEY, searchKeyword.searchKeyword)

            val response = retrofit.execute()

            if (response.isSuccessful) {
                 response.body()?.places ?: emptyList()
            }else{
                emptyList()
            }
        }
    }
}