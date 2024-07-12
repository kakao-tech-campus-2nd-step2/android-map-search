package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MainActivity : AppCompatActivity() {


    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** 중요 */
        var keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)                 // 해쉬값 알아내기
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)


        mapView = findViewById(R.id.map_view)
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d("testt", "onMapDestory")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("testt", "onMapError")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("testt", "onMapReady")
            }
        })


        val searchEditText: EditText = findViewById(R.id.searchEditText)
        // EditText를 비활성화합니다.
        searchEditText.isFocusable = false
        searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d("testt", "onResume")
        mapView.resume() // MapView 의 resume 호출
    }

    public override fun onPause() {
        super.onPause()
        Log.d("testt", "onPause")
        mapView.pause() // MapView 의 pause 호출
    }

}
