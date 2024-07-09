package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class MapViewActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MapViewActivity", "onCreate called")


        Log.d("MapViewActivity", "Setting content view")
        setContentView(R.layout.activity_map_view)
        Log.d("MapViewActivity", "Content view set")


        try {
            mapView = findViewById(R.id.map)
            Log.d("MapViewActivity", "mapView initialized")
        } catch (e: Exception) {
            Log.e("MapViewActivity", "Error initializing mapView", e)
        }

        try {
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "카카오맵 정상종료")
                }

                override fun onMapError(exception: Exception?) {
                    if (exception is MapAuthException) {
                        handleAuthError(exception)
                    } else {
                        Log.e("KakaoMap", "카카오맵 인증실패", exception)
                    }
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d("KakaoMap", "카카오맵 정상실행")
                }
            })
            Log.d("MapViewActivity", "mapView start called")
        } catch (e: Exception) {
            Log.e("MapViewActivity", "Exception during mapView.start", e)
        }
    }

    private fun handleAuthError(exception: MapAuthException) {
        when (exception.errorCode) {
            -1 -> Log.e("KakaoMap", "인증 과정 중 원인을 알 수 없는 에러가 발생한 상태")
            -2 -> Log.e("KakaoMap", "통신 연결 시도 중 발생하는 에러")
            -3 -> Log.e("KakaoMap", "통신 연결 중 SocketTimeoutException 에러가 발생한 경우")
            -4 -> Log.e("KakaoMap", "통신 시도 중 ConnectTimeoutException 에러가 발생한 경우")
            400 -> Log.e("KakaoMap", "일반적인 오류. 주로 API에 필요한 필수 파라미터와 관련하여 서버가 클라이언트 오류를 감지해 요청을 처리하지 못한 상태입니다.")
            401 -> Log.e("KakaoMap", "인증 오류. 해당 리소스에 유효한 인증 자격 증명이 없어 요청에 실패한 상태")
            403 -> Log.e("KakaoMap", "권한 오류. 서버에 요청이 전달되었지만, 권한 때문에 거절된 상태")
            429 -> Log.e("KakaoMap", "쿼터 초과. 정해진 사용량이나 초당 요청 한도를 초과한 경우")
            499 -> Log.e("KakaoMap", "통신 실패 오류. 인터넷 연결 상태 확인이 필요한 경우")
            else -> Log.e("KakaoMap", "알 수 없는 인증 에러 발생: ${exception.errorCode}")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MapViewActivity", "onResume called")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MapViewActivity", "onPause called")
        mapView.pause()
    }
}
