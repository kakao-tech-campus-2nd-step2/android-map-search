package campus.tech.kakao.map.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}