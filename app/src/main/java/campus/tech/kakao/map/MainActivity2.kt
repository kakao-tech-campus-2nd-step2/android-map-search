package campus.tech.kakao.map

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchViewModel: SearchViewModel
    val closebtn = findViewById<Button>(R.id.close)
    val textenter = findViewById<EditText>(R.id.text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchRepository = SearchRepo(DatabaseHelper())
        searchViewModel = SearchViewModel.getInstance(searchRepository)
        searchViewModel.insertSearchData("팜하니")
        searchViewModel.insertSearchData("판교")
        searchViewModel.insertSearchData("카카오")
        searchViewModel.insertSearchData("병원")

        // close 버튼 클릭 리스너 추가
        closebtn.setOnClickListener { clearSearchText() }
    }

    private fun clearSearchText() {
        textenter.clearComposingText()

    }

    private fun DatabaseHelper(): DatabaseHelper {
        TODO("Not yet implemented")
    }


}

