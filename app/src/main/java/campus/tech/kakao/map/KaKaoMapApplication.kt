package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class KaKaoMapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}