package campus.tech.kakao.map.Model

import android.app.Application
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            KakaoMapSdk.init(this, "b6f4b70a2090ca9cfca380ec1ebc9c5f")
            Log.d("KakaoMapSDK", "SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("KakaoMapSDK", "error", e)
        }

    }
}