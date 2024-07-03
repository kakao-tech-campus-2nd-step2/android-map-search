package campus.tech.kakao.map.model

import android.provider.BaseColumns

object Search : BaseColumns{
    const val TABLE_NAME = "search_results"
    const val COLUMN_ID = "id"
    const val COLUMN_KEYWORD = "keyword"
    const val COLUMN_TIMESTAMP = "timestamp"
}