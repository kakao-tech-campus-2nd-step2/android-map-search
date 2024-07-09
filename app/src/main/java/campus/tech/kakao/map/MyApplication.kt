package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.di.AppContainer
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        KakaoMapSdk.init(this,campus.tech.kakao.map.BuildConfig.KAKAO_API_KEY)
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}