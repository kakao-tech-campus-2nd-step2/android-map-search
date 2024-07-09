package campus.tech.kakao.map

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var DBHelper: DBHelper
    lateinit var HistoryDBHelper : HistoryDBHelper
    lateinit var DB: SQLiteDatabase
    lateinit var HistoryDB : SQLiteDatabase
    lateinit var adapter: RecycleAdapter
    lateinit var horadapter : HorRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Delete = binding.Delete
        val SearchText = binding.SearchText

        Delete.setOnClickListener{
            SearchText.setText(null)
        }

        DBHelper = DBHelper(this, "place.db", null, 2)
        HistoryDBHelper = HistoryDBHelper(this,"history.db",null,2)

        MakeDummyData("카페")
        MakeDummyData("약국")

        SetupView()
    }

    fun SetupView() {
        val Search = binding.SearchText
        val NoSearchText = binding.NoSearchText
        val RecyclerView = binding.RecyclerView
        val HolRecyclerView = binding.HorRecyclerView

        adapter = RecycleAdapter(){ name ->
            if (SearchHistory(name) == 0) {
                insertHistory(name)
                SubAllHistory()
            }
        }

        horadapter = HorRecycleAdapter(){ name ->
            DeleteItem(name)
        }
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = LinearLayoutManager(this)

        HolRecyclerView.adapter = horadapter
        HolRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)

        SubAllHistory()


        Search.addTextChangedListener {
            val searchText = it.toString().trim()
            if (searchText.isEmpty()) {
                NoSearchText.visibility = View.VISIBLE
                RecyclerView.visibility = View.INVISIBLE

            } else {
                NoSearchText.visibility = View.INVISIBLE
                RecyclerView.visibility = View.VISIBLE
                SearchPlaces(searchText)
            }
        }
    }

    fun SearchPlaces(Keyword: String) {
        val selection = "${PlaceEntry.COLUMN_CATEGORY} LIKE ?"
        val selectionArgs = arrayOf("%$Keyword%")
        DB = DBHelper.readableDatabase
        val cursor = DB.query(
            PlaceEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        adapter.SubmitCursor(cursor)
    }

    fun SearchHistory(name : String): Int {
        val selection = "${HistoryEntry.COLUMN_NAME} = ?"
        val selectionArgs = arrayOf(name)
        HistoryDB = HistoryDBHelper.readableDatabase
        val cursor = HistoryDB.query(
            HistoryEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            return 1
        }
        return 0
    }

    fun SubAllHistory(){
        HistoryDB = HistoryDBHelper.readableDatabase
        val cursor = HistoryDB.query(
            HistoryEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        horadapter.SubmitCursor(cursor)
    }
    fun MakeDummyData(category: String) {
        DB = DBHelper.writableDatabase
        for (i in 1..20) {
            val place = Place(category + i, "대구 경북대학교로 $i 길", category)
            insertPlace(place)
        }
    }

    fun insertPlace(place: Place) {
        val values = ContentValues()
        values.put(PlaceEntry.COLUMN_NAME, place.Name)
        values.put(PlaceEntry.COLUMN_ADDRESS, place.Address)
        values.put(PlaceEntry.COLUMN_CATEGORY, place.Category)
        DB.insert(PlaceEntry.TABLE_NAME, null, values)
    }

    fun insertHistory(name : String){
        HistoryDB = HistoryDBHelper.writableDatabase
        val values = ContentValues()
        values.put(HistoryEntry.COLUMN_NAME,name)
        HistoryDB.insert(HistoryEntry.TABLE_NAME,null,values)
    }

    fun DeleteItem(name : String){
        val selection = "${HistoryEntry.COLUMN_NAME} = ?"
        val selectionArgs = arrayOf(name)

        HistoryDB = HistoryDBHelper.writableDatabase

        val deleteRows = HistoryDB.delete(
            HistoryEntry.TABLE_NAME,
            selection,
            selectionArgs
        )

        if (deleteRows > 0){
            SubAllHistory()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
