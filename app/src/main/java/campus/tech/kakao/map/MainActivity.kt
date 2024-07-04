package campus.tech.kakao.map

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    lateinit var tvNoResult: TextView
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etSearch = findViewById<EditText>(R.id.etSearch)

        dbHelper = DBHelper(this)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()
                searchProfiles(search)
            }
        })

    }
    fun searchProfiles(query: String) {
        if (query.isEmpty()) {
            tvNoResult.visibility = TextView.VISIBLE
            adapter.updateProfiles(emptyList())
        } else {
            val profileList = dbHelper.searchProfiles(query)
            if (profileList.isEmpty()) {
                tvNoResult.visibility = TextView.VISIBLE
            } else {
                tvNoResult.visibility = TextView.GONE
            }
            adapter.updateProfiles(profileList)
        }
    }
}
