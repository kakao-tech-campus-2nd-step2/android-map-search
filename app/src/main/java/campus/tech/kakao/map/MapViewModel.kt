package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DbHelper(application)

    fun insertData() {
        dbHelper.insertData()
    }
}
