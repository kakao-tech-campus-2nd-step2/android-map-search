package campus.tech.kakao.map

import android.app.Application
import android.content.SharedPreferences
import com.kakao.vectormap.KakaoMapSdk

class MapApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceManager
    }

    override fun onCreate() {
        prefs = PreferenceManager(applicationContext)
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_REST_API_KEY);
    }
}