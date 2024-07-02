package campus.tech.kakao.map

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchViewModel: SearchViewModel

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
        binding.close.setOnClickListener { clearSearchText() }
    }

    private fun DatabaseHelper(): DatabaseHelper {
        TODO("Not yet implemented")
    }

    private fun clearSearchText() {
        // EditText의 텍스트를 빈 문자열로 설정하여 입력된 글자를 지움
        binding.searchEditText.setText("")
    }
}

