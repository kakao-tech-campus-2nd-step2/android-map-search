package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object MapContract {
    const val TABLE_CAFE = "cafe"
    const val TABLE_PHARMACY = "pharmacy"
    const val COLUMN_NAME = "name"
    const val COLUMN_ADDRESS = "address"
    const val COLUMN_CATEGORY = "category"
}

class Database(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {

    private val kakaoApiKey = "e6a7c826ae7a55df129b8be2c636e213"
    private val okHttpClient = OkHttpClient()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_CAFE} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_PHARMACY} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        addDummyData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_CAFE}")
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_PHARMACY}")
        onCreate(db)
    }

    private fun addDummyData(db: SQLiteDatabase?) {
        GlobalScope.launch(Dispatchers.IO) {
            addDummyCafes(db)
            addDummyPharmacies(db)
        }
    }

    private fun addDummyCafes(db: SQLiteDatabase?) {
        fetchPlaces("카페", "광주 북구 용봉동") { places ->
            places.forEach { place ->
                val values = ContentValues().apply {
                    put(MapContract.COLUMN_NAME, place.optString("place_name"))
                    put(MapContract.COLUMN_ADDRESS, place.optString("address_name"))
                    put(MapContract.COLUMN_CATEGORY, "카페")
                }
                db?.insert(MapContract.TABLE_CAFE, null, values)
            }
        }
    }

    private fun addDummyPharmacies(db: SQLiteDatabase?) {
        fetchPlaces("약국", "경상남도 함안군 칠원읍") { places ->
            places.forEach { place ->
                val values = ContentValues().apply {
                    put(MapContract.COLUMN_NAME, place.optString("place_name"))
                    put(MapContract.COLUMN_ADDRESS, place.optString("address_name"))
                    put(MapContract.COLUMN_CATEGORY, "약국")
                }
                db?.insert(MapContract.TABLE_PHARMACY, null, values)
            }
        }
    }

    private fun fetchPlaces(category: String, region: String, callback: (List<JSONObject>) -> Unit) {
        val url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=${category}&region=${region}&page=1"
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "KakaoAK $kakaoApiKey")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body?.string()
                val jsonObject = JSONObject(jsonResponse)
                val places = jsonObject.optJSONArray("documents")?.toList() ?: emptyList()
                callback(places)
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun searchPlaces(searchText: String): List<Map<String, String>> {
        val db = readableDatabase
        val cafes = queryPlaces(db, MapContract.TABLE_CAFE, searchText)
        val pharmacies = queryPlaces(db, MapContract.TABLE_PHARMACY, searchText)
        db.close()

        val dataList = mutableListOf<Map<String, String>>()
        dataList.addAll(cafes)
        dataList.addAll(pharmacies)
        return dataList
    }

    private fun queryPlaces(
        db: SQLiteDatabase,
        tableName: String,
        searchText: String
    ): List<Map<String, String>> {
        val cursor: Cursor = db.query(
            tableName,
            null,
            "${MapContract.COLUMN_NAME} LIKE ? OR ${MapContract.COLUMN_ADDRESS} LIKE ?",
            arrayOf("%$searchText%", "%$searchText%"),
            null,
            null,
            null
        )

        val results = mutableListOf<Map<String, String>>()
        while (cursor.moveToNext()) {
            val row = mutableMapOf<String, String>()
            row[MapContract.COLUMN_NAME] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_NAME))
            row[MapContract.COLUMN_ADDRESS] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_ADDRESS))
            row[MapContract.COLUMN_CATEGORY] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_CATEGORY))
            results.add(row)
        }
        cursor.close()
        return results
    }
}

