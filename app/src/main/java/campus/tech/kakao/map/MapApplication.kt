package campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk


import com.kakao.sdk.common.util.Utility

class MapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, campus.tech.kakao.map.BuildConfig.KAKAO_API_KEY)
    }

}

