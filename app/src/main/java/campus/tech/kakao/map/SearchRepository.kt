package campus.tech.kakao.map

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    fun saveSearchWord(searchWord: SearchWord) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SearchWordEntry.SEARCH_WORD, searchWord.searchword)
        }
        db.insert(SearchWordEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun getSavedSearchWords(): List<SearchWord> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            SearchWordEntry.SEARCH_WORD
        )

        val cursor = db.query(
            SearchWordEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val savedSearchWords = mutableListOf<SearchWord>()
        with(cursor) {
            while (moveToNext()) {
                val searchWord = getString(getColumnIndexOrThrow(SearchWordEntry.SEARCH_WORD))

                val savedSearchWord = SearchWord(searchWord)
                savedSearchWords.add(savedSearchWord)
            }
        }
        cursor.close()
        db.close()
        return savedSearchWords
    }

    fun delSavedSearchWord(searchWord: SearchWord) {
        val db = dbHelper.writableDatabase
        val selection = "${SearchWordEntry.SEARCH_WORD} = ?"
        val selectionArgs = arrayOf(searchWord.searchword)
        db.delete(SearchWordEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}