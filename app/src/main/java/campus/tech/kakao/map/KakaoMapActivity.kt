package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class KakaoMapActivity : AppCompatActivity() {
    var mapView: MapView? = null
    var kakaoMap: KakaoMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map)
        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this,appKey)
        mapView = findViewById(R.id.map_view)
        mapView?.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API가 정상적으로 종료될 때 호출
                    Log.d("KakaoMap", "onMapDestroy: ")
                }

                override fun onMapError(error: Exception) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                    Log.e("KakaoMap", "onMapError: ", error)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    // 정상적으로 인증이 완료되었을 때 호출
                    // KakaoMap 객체를 얻어 옵니다.
                    kakaoMap = map
                }
            })

    }   //onCreate

    override fun onResume() {
        super.onResume()
        mapView?.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
    }

}