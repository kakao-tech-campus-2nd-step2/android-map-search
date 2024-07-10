package campus.tech.kakao.map

import android.app.Application
import android.content.SharedPreferences

class MapApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceManager
    }

    override fun onCreate() {
        prefs = PreferenceManager(applicationContext)
        super.onCreate()
    }
}