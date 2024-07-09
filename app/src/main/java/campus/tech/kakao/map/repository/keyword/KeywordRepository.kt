package com.example.kakaotechcampus_2nd_step2_android.repository.keyword

import android.content.ContentValues
import android.content.Context

class KeywordRepository(context: Context) {

    private val dbHelper = KeywordDbHelper(context)

    fun update(keyword: String) {
        val db = dbHelper.writableDatabase
        ContentValues().apply {
            put(KeywordContract.RECENT_KEYWORD, keyword)
            db.insert(KeywordContract.TABLE_NAME, null, this)
        }
    }

    fun read(): List<String> {
        val keywords = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        val projection = arrayOf(KeywordContract.RECENT_KEYWORD)
        val cursor = db.query(
            KeywordContract.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                keywords.add(getString(getColumnIndexOrThrow(KeywordContract.RECENT_KEYWORD)))
            }
        }
        return keywords
    }

    fun delete(keyword: String) {
        val db = dbHelper.writableDatabase

        db.delete(
            KeywordContract.TABLE_NAME,
            "${KeywordContract.RECENT_KEYWORD} = ?",
            arrayOf(keyword)
        )
    }

    companion object {
        fun getInstance(context: Context): KeywordRepository {
            return KeywordRepository(context)
        }
    }
}
