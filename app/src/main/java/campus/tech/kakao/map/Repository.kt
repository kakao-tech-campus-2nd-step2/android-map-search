package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Repository(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)
    private val db = dbHelper.writableDatabase
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)

    fun populateInitialData() {
        val dataCategories = listOf("카페", "약국", "영화관")

        db.beginTransaction()
        for (category in dataCategories) {
            for (i in 1..10) {
                val name = "$category$i"
                val address = "대전 유성구 봉명동 $i"
                if (!isDataExist(name)) {
                    val insertData = "INSERT INTO ${DatabaseHelper.TABLE_NAME} (${DatabaseHelper.COLUMN_NAME}, ${DatabaseHelper.COLUMN_ADDRESS}) " +
                            "VALUES ('$name', '$address')"
                    db.execSQL(insertData)
                }
            }
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    private fun isDataExist(name: String): Boolean {
        val cursor = db.rawQuery("SELECT 1 FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_NAME} = ?", arrayOf(name))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun search(query: String): List<Keyword> {
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_NAME} LIKE ?", arrayOf("%$query%"))
        val keywords = mutableListOf<Keyword>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
                keywords.add(Keyword(id, name, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return keywords
    }

    fun saveKeywordToPrefs(keyword: Keyword) {
        val savedKeywords = getAllSavedKeywordsFromPrefs().toMutableList()
        savedKeywords.add(0, keyword)
        val editor = sharedPreferences.edit()
        editor.putString("saved_keywords", Gson().toJson(savedKeywords))
        editor.apply()
    }

    fun deleteKeywordFromPrefs(keyword: Keyword) {
        val savedKeywords = getAllSavedKeywordsFromPrefs().toMutableList()
        savedKeywords.remove(keyword)
        val editor = sharedPreferences.edit()
        editor.putString("saved_keywords", Gson().toJson(savedKeywords))
        editor.apply()
    }

    fun getAllSavedKeywordsFromPrefs(): List<Keyword> {
        val savedKeywordsJson = sharedPreferences.getString("saved_keywords", null) ?: return emptyList()
        val type = object : TypeToken<List<Keyword>>() {}.type
        return Gson().fromJson(savedKeywordsJson, type)
    }
}