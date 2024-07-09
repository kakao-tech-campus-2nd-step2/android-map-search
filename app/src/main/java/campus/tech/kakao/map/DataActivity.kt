package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DataActivity : AppCompatActivity() {
    private lateinit var placeDatabaseAccess: PlaceDatabaseAccess
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        placeDatabaseAccess = PlaceDatabaseAccess(this)

        for (index in 1..10) {
            placeDatabaseAccess.insertPlace(PlaceDataModel("카페$index", "서울 성동구 성수동 $index", "카페"))
            placeDatabaseAccess.insertPlace(PlaceDataModel("약국$index", "서울 강남구 대치동 $index", "약국"))
        }

        val intent = Intent(this, PlaceActivity::class.java)
        startActivity(intent)

    }
}