package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "History.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE search_results (id INTEGER PRIMARY KEY, text TEXT)")
        db?.execSQL("CREATE TABLE saved_searches (id INTEGER PRIMARY KEY, text TEXT)")
        addDummySearchData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS search_results")
        db?.execSQL("DROP TABLE IF EXISTS saved_searches")
        onCreate(db)
    }

    private fun addDummySearchData(db: SQLiteDatabase?) {
        val dummySearchData = listOf(
            "Android", "Kotlin", "Kakao", "약국", "카페", "아무거나", "일단 테스트"
        )
        for (searchText in dummySearchData) {
            db?.execSQL("INSERT INTO saved_searches (text) VALUES (?)", arrayOf(searchText))
        }
    }

    fun insertSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO search_results (text) VALUES (?)", arrayOf(searchText))
        db.close()
    }

    fun deleteSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM search_results WHERE text = ?", arrayOf(searchText))
        db.close()
    }


    fun getSavedSearches(): List<String> {
        val db = readableDatabase
        val cursor = db.query("saved_searches", arrayOf("text"), null, null, null, null, null)
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }
    fun getSearchResults(searchText: String): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            "search_results",
            arrayOf("text"),
            "text LIKE ?",
            arrayOf("%$searchText%"),
            null,
            null,
            null
        )
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }

    class SearchAdapter(
        private val context: Context,
        private var data: List<String>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<SearchItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_item_view, parent, false)
            return SearchItemViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        fun updateData(newData: List<String>) {
            data = newData
            notifyDataSetChanged()
        }
    }

    class SearchItemViewHolder(itemView: View, private val onItemClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val textView: TextView = itemView.findViewById(R.id.result)
        private val deleteButton: Button = itemView.findViewById(R.id.delete)

        fun bind(searchText: String) {
            textView.text = searchText
            deleteButton.setOnClickListener {
                onItemClick(searchText)
            }
        }
    }

    // saved_searches 테이블의 데이터를 RecyclerView에 바인딩하기 위한 Adapter 추가
    class SavedSearchAdapter(
        private val context: Context,
        private var data: List<String>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<SavedSearchAdapter.SavedSearchViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_item_view, parent, false)
            return SavedSearchViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: SavedSearchViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        fun updateData(newData: List<String>) {
            data = newData
            notifyDataSetChanged()
        }

        inner class SavedSearchViewHolder(itemView: View, private val onItemClick: (String) -> Unit) :
            RecyclerView.ViewHolder(itemView) {

            private val textView: TextView = itemView.findViewById(R.id.result)

            fun bind(searchText: String) {
                textView.text = searchText
                itemView.setOnClickListener {
                    onItemClick(searchText)
                }
            }
        }
    }
}

