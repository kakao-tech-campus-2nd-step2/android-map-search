package campus.tech.kakao.map

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var input: TextView
    private lateinit var researchCloseButton: ImageView
    private lateinit var scrollviewContainer: LinearLayout
    private lateinit var resultLayout: LinearLayout
    private lateinit var noResultTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeRepository: PlaceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        researchCloseButton = findViewById(R.id.close_button)
        scrollviewContainer = findViewById(R.id.tab_container)
        noResultTextView = findViewById(R.id.no_result_textview)
        recyclerView = findViewById(R.id.recyclerView)

        val dbHelper = PlaceDbHelper(this)

        placeRepository = PlaceRepository(this)

        placeRepository.insertInitialData()

    }


}
