package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var etSearch: EditText
    private val startZoomLevel = 15
    private val startPosition = LatLng.from(35.234, 129.0807)


    // 지도가 정상적으로 시작된 후 수신
    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
        }

        override fun getPosition(): LatLng {
            return startPosition
        }

        override fun getZoomLevel(): Int {
            return startZoomLevel
        }
    }

    // 지도의 LifeCycle 관련 이벤트 수신
    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapResumed() {
            super.onMapResumed()
            mapView.resume()
        }

        override fun onMapPaused() {
            super.onMapPaused()
            mapView.pause()
        }

        override fun onMapDestroy() {
            Toast.makeText(
                applicationContext, "onMapDestroy",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onMapError(error: Exception) {
            Toast.makeText(
                applicationContext, error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        mapView = findViewById(R.id.mapView)
        etSearch = findViewById(R.id.etSearch)
        mapView.start(lifeCycleCallback, readyCallback)
        etSearch.setOnClickListener {
            val searchIntent = Intent(this, PlaceActivity::class.java)
            startActivity(searchIntent)
        }
    }
}