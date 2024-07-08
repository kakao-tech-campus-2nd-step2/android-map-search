package campus.tech.kakao.map

import android.provider.BaseColumns

data class SearchWord(
    val searchword: String
)

object SearchWordEntry : BaseColumns {
    const val TABLE_NAME = "search_word"
    const val SEARCH_WORD = "search_word"
}