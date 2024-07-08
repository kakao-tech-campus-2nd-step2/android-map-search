package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns

class SearchRepository(context: Context) {

    private val dbHelper = StoreInfoDBHelper(context)

    fun setDefaultData() {
        if(hasStoreInfo()) return
        for (i in 1..100) {
            val storeInfo = StoreInfo("cafe ${i}th street", "충남대학교", "cafe")
            addStoreInfo(storeInfo)
        }
    }

    fun hasStoreInfo(): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            StoreInfoEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID),
            null,
            null,
            null,
            null,
            null
        )
        val result = cursor.count != 0
        cursor.close()
        db.close()
        return result
    }

    fun addStoreInfo(storeInfo: StoreInfo) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(StoreInfoEntry.STORE_NAME, storeInfo.name)
            put(StoreInfoEntry.STORE_LOCATION, storeInfo.location)
            put(StoreInfoEntry.STORE_CATEGORY, storeInfo.category)
        }
        db.insert(StoreInfoEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun getSearchResults(searchWord: SearchWord): List<StoreInfo> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            StoreInfoEntry.STORE_NAME,
            StoreInfoEntry.STORE_LOCATION,
            StoreInfoEntry.STORE_CATEGORY
        )

        val selection = "${StoreInfoEntry.STORE_CATEGORY} = ?"
        val selectionArgs = arrayOf(searchWord.searchword)

        val cursor = db.query(
            StoreInfoEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val searchResults = mutableListOf<StoreInfo>()
        with(cursor) {
            while (moveToNext()) {
                val storeName = getString(getColumnIndexOrThrow(StoreInfoEntry.STORE_NAME))
                val storeLocation = getString(getColumnIndexOrThrow(StoreInfoEntry.STORE_LOCATION))
                val storeCategory = getString(getColumnIndexOrThrow(StoreInfoEntry.STORE_CATEGORY))

                val storeInfo = StoreInfo(storeName, storeLocation, storeCategory)
                searchResults.add(storeInfo)
            }
        }
        cursor.close()
        db.close()
        return searchResults
    }

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