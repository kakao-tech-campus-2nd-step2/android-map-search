package campus.tech.kakao.map.repository

import campus.tech.kakao.map.dto.ResultSearchKeyword
import campus.tech.kakao.map.network.KakaoLocalApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoRepository {

    private val api: KakaoLocalApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(KakaoLocalApi::class.java)
    }

    fun searchKeyword(query: String, callback: retrofit2.Callback<ResultSearchKeyword>) {
        val call = api.searchKeyword(query)
        call.enqueue(callback)
    }
}
