package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class MapActivity : AppCompatActivity() {
	private lateinit var mapView: MapView
	private var map: KakaoMap? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)
		mapView = findViewById(R.id.map_view)
		mapView.start(object : MapLifeCycleCallback() {
			override fun onMapDestroy() {
				TODO("Not yet implemented")
			}

			override fun onMapError(p0: Exception?) {
				Log.d("testt", "onMapError: " + p0.toString())
			}

		}, object: KakaoMapReadyCallback() {
			override fun onMapReady(p0: KakaoMap) {
				map = p0
			}

		})
	}

	override fun onResume() {
		super.onResume()
		mapView.resume()
	}

	override fun onPause() {
		super.onPause()
		mapView.pause()
	}

}