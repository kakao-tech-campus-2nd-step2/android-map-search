package campus.tech.kakao.map

import android.provider.BaseColumns

class PlaceContract {
    object Place : BaseColumns {
        const val TABLE_NAME = "place"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
    }

    object Search : BaseColumns {
        const val TABLE_NAME = "search"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
    }
}
