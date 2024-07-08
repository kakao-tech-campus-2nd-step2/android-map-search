package campus.tech.kakao.map.DTO

import android.provider.BaseColumns

object PlaceContract:BaseColumns {
	const val DB_NAME = "place.db"
	const val DB_VERSION = 2
	const val TABLE_NAME = "place"
	const val COLUMN_NAME_NAME = "name"
	const val COLUMN_NAME_ADDRESS = "address"
	const val COLUMN_NAME_TYPE = "type"
}