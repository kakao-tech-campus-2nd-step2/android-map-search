package campus.tech.kakao.map

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private val API_KEY = "276613de22bf074fb398b9eed102f89b"

    private lateinit var db: DataDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var cancelBtn: Button
    private lateinit var resultView: TextView
    private lateinit var searchView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var searchAdapter: SearchViewAdapter
    private var locationList = ArrayList<LocationData>()
    private var searchList = ArrayList<LocationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        loadSearchList()
        setCancelBtn()
        setSearchView()
        setRecyclerView()
        setSearchListener()
    }
    private fun initialize() {
        recyclerView = findViewById(R.id.recyclerView)
        inputText = findViewById(R.id.inputText)
        cancelBtn = findViewById(R.id.cancelBtn)
        resultView = findViewById(R.id.resultView)
        searchView = findViewById(R.id.searchView)
    }


    private fun setCancelBtn() {
        cancelBtn.setOnClickListener {
            inputText.text.clear()
        }
    }

    private fun setRecyclerView() {
        locationAdapter = LocationAdapter(locationList) { locationData ->
            onItemClick(locationData)
        }
        recyclerView.adapter = locationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setSearchView() {
        searchAdapter = SearchViewAdapter(searchList) { removedItem ->
            val index = searchList.indexOf(removedItem)
            if (index != -1) {
                searchList.removeAt(index)
                searchAdapter.notifyItemRemoved(index)
                if (searchList.isEmpty()) {
                    searchView.visibility = View.GONE
                }
                saveSearchList()
            }
        }
        searchView.adapter = searchAdapter
        searchView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun saveSearchList() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonString = Gson().toJson(searchList)
        editor.putString("search_list", jsonString)
        editor.apply()
        Log.d("MainActivity", "Search list saved")
    }

    private fun loadSearchList() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("search_list", null)
        if (jsonString != null) {
            val type = object : TypeToken<ArrayList<LocationData>>() {}.type
            searchList = Gson().fromJson(jsonString, type)
        }
        Log.d("searchList", "$searchList")

        if (searchList.isNotEmpty()) {
            searchView.visibility = View.VISIBLE
            Log.d("MainActivity", "SearchView should be visible, items: ${searchList.size}")
        } else {
            searchView.visibility = View.GONE
            Log.d("MainActivity", "SearchView should be invisible, items: ${searchList.size}")
        }
    }

    private fun setSearchListener() {
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchLocations(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun onItemClick(locationData: LocationData) {
        if (!searchList.contains(locationData)) {
            searchList.add(0, locationData)
            searchAdapter.notifyItemInserted(0)
            searchView.scrollToPosition(0)
        } else {
            val index = searchList.indexOf(locationData)
            if (index > 0) {
                searchList.removeAt(index)
                searchList.add(0, locationData)
                searchAdapter.notifyItemMoved(index, 0)
                searchView.scrollToPosition(0)
            }
        }
        searchView.visibility = View.VISIBLE
        saveSearchList()
        Log.d("MainActivity", "Item clicked: ${locationData.name}")
    }

    private fun searchLocations(key: String) {
        val readDB = db.readableDatabase
        locationList.clear()

        val cursor = readDB.query(
            "LOCATION",
            null,
            "name LIKE ? OR location LIKE ? OR category LIKE ?",
            arrayOf("%$key%", "%$key%", "%$key%"),
            null,
            null,
            null
        )

        cursor.use { cur ->
            while (cur.moveToNext()) {
                val name = cur.getString(cur.getColumnIndexOrThrow("name"))
                val location = cur.getString(cur.getColumnIndexOrThrow("location"))
                val category = cur.getString(cur.getColumnIndexOrThrow("category"))
                locationList.add(LocationData(name, location, category))
            }
        }

        updateRecyclerView()
        isShowText()
    }

    private fun updateRecyclerView() {
        locationAdapter.notifyDataSetChanged()
    }

    private fun isShowText() {
        if (locationList.isEmpty()) {
            resultView.visibility = View.VISIBLE
        } else {
            resultView.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        Log.d("MainActivity", "Database closed")
    }
}