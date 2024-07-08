package campus.tech.kakao.map

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    lateinit var dbHelper: DBHelper
    lateinit var tvNoResult: TextView
    lateinit var llSave: LinearLayoutCompat
    lateinit var hScrollView: HorizontalScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        tvNoResult = findViewById(R.id.tvNoResult)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnClose = findViewById<Button>(R.id.btnClose)
        llSave = findViewById(R.id.llSave)
        hScrollView = findViewById(R.id.hScrollView)

        adapter = Adapter(emptyList())
        recyclerView.adapter = adapter

        tvNoResult.visibility = TextView.VISIBLE

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()
                searchProfiles(search)
            }
        })

        adapter.setOnItemClickListener(object :Adapter.OnItemClickListener {
            override fun onItemClick(name: String) {
                if (isProfileInSearchSave(name)) {
                    removeSavedItem(name)
                }
                addSavedItem(name)
            }
        })

        btnClose.setOnClickListener{
            etSearch.text.clear()
        }
        loadSavedItems()
    }
    override fun onPause() {
        super.onPause()
        saveSavedItems()
    }

    fun saveSavedItems() {
        val sharedPreferences = getSharedPreferences("SavedItems", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val savedNames = JSONArray()
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName != null) {
                savedNames.put(tvSaveName.text.toString())
            }
        }
        editor.putString("savedNames", savedNames.toString())
        editor.apply()
    }

    fun loadSavedItems(){
        val sharedPreferences = getSharedPreferences("SavedItems", MODE_PRIVATE)
        val savedNamesString = sharedPreferences.getString("savedNames", "[]")
        val savedNames = JSONArray(savedNamesString)
        for (i in 0 until savedNames.length()) {
            val name = savedNames.getString(i)
            addSavedItem(name)
        }
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
    fun addSavedItem(name: String) {
        val savedView = LayoutInflater.from(this)
            .inflate(R.layout.search_save, llSave, false) as ConstraintLayout

        val tvSaveName = savedView.findViewById<TextView>(R.id.tvSaveName)
        val ivDelete = savedView.findViewById<ImageView>(R.id.ivDelete)

        tvSaveName.text = name
        ivDelete.setOnClickListener {
            llSave.removeView(savedView)
        }

        llSave.addView(savedView)
        hScrollView.visibility = View.VISIBLE
        scrollToEndOfSearchSave()
    }
    fun removeSavedItem(name: String) {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                llSave.removeViewAt(i)
                break
            }
        }
    }
    fun isProfileInSearchSave(name: String): Boolean {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                return true
            }
        }
        return false
    }

    fun scrollToEndOfSearchSave() {
        hScrollView.post {
            hScrollView.fullScroll(View.FOCUS_RIGHT)
        }
    }
}
