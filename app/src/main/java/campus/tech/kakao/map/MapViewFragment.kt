package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import campus.tech.kakao.map.databinding.FragmentMapViewBinding
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class MapViewFragment : Fragment() {
    private var _binding: FragmentMapViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        KakaoMapSdk.init(requireContext(), BuildConfig.KAKAO_NATIVE_API_KEY)

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // MapView lifecycle callback: onDestroy
            }

            override fun onMapError(error: Exception) {
                // MapView lifecycle callback: onError
                Log.e("testt", "Error initializing map", error)
                error.printStackTrace()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: com.kakao.vectormap.KakaoMap) {
                // KakaoMap is ready to use
                Log.d("testt", "Map is ready")

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
