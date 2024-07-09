package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.model.SavedSearchWord

class SavedSearchWordRepository(private val dbHelper: SavedSearchWordDBHelper) {
    fun insertOrUpdateSearchWord(searchWord: SavedSearchWord) {
        dbHelper.insertOrUpdateSearchWord(searchWord)
    }

    fun getAllSearchWords(): List<SavedSearchWord> {
        return dbHelper.getAllSearchWords()
    }

    fun deleteSearchWordById(id: Long) {
        dbHelper.deleteSearchWordById(id)
    }
}
