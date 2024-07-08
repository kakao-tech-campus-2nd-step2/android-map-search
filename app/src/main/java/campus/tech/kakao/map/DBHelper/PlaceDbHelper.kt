package campus.tech.kakao.map.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.DTO.Place
import campus.tech.kakao.map.DTO.PlaceContract
import campus.tech.kakao.map.DTO.PlaceContract.DB_VERSION

class PlaceDbHelper(context: Context):SQLiteOpenHelper(
	context, PlaceContract.DB_NAME, null, DB_VERSION) {
	private val _place = MutableLiveData<List<Place>>()
	override fun onCreate(db: SQLiteDatabase?) {
		createTable(db)
	}
	private fun createTable(db: SQLiteDatabase?) {
		db?.execSQL("CREATE TABLE ${PlaceContract.TABLE_NAME} " +
				"(${PlaceContract.COLUMN_NAME_NAME} TEXT, " +
				"${PlaceContract.COLUMN_NAME_ADDRESS} TEXT, " +
				"${PlaceContract.COLUMN_NAME_TYPE} TEXT)")
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		db?.execSQL("DROP TABLE IF EXISTS ${PlaceContract.TABLE_NAME}")
		createTable(db)
	}

	fun getPlace(): MutableLiveData<List<Place>> {
		return _place
	}

	fun addPlace(place: Place) {
		val db = writableDatabase
		if (!existPlace(place, db)){
			val values = ContentValues()
			values.put(PlaceContract.COLUMN_NAME_NAME, place.name)
			values.put(PlaceContract.COLUMN_NAME_ADDRESS, place.address)
			values.put(PlaceContract.COLUMN_NAME_TYPE, place.type)
			db.insert(PlaceContract.TABLE_NAME, null, values)
			db.close()
		}
	}

	fun existData(): Boolean{
		val db = readableDatabase
		val cursor = db.query(
			PlaceContract.TABLE_NAME,
			arrayOf(PlaceContract.COLUMN_NAME_NAME),
			null,
			null,
			null,
			null,
			null
		)
		val result = cursor.moveToFirst()
		cursor.close()
		return result
	}

	fun existPlace(place: Place, db: SQLiteDatabase): Boolean{
		val selection = "${PlaceContract.COLUMN_NAME_NAME} = ? AND " +
				"${PlaceContract.COLUMN_NAME_ADDRESS} = ? AND " +
				"${PlaceContract.COLUMN_NAME_TYPE} = ?"
		val cursor = db.query(
			PlaceContract.TABLE_NAME,
			arrayOf(PlaceContract.COLUMN_NAME_NAME),
			selection,
			arrayOf(place.name, place.address, place.type),
			null,
			null,
			"${PlaceContract.COLUMN_NAME_NAME} DESC"
		)

		val result = cursor.moveToFirst()
		cursor.close()
		return if (result) true else false
	}

	fun searchPlaceName(name: String){
		val resultList = mutableListOf<Place>()
		val searchResult = "%${name}%"
		val cursor = readableDatabase.query(
			PlaceContract.TABLE_NAME,
			arrayOf(
				PlaceContract.COLUMN_NAME_NAME,
				PlaceContract.COLUMN_NAME_ADDRESS,
				PlaceContract.COLUMN_NAME_TYPE
			),
			"${PlaceContract.COLUMN_NAME_NAME} like ?",
			arrayOf(searchResult),
			null,
			null,
			"${PlaceContract.COLUMN_NAME_NAME} ASC"
		)

		while (cursor.moveToNext()) {
			val name = cursor.getString(
				cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME_NAME)
			)
			val address = cursor.getString(
				cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME_ADDRESS)
			)
			val type = cursor.getString(
				cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME_TYPE))
			resultList.add(Place(name, address, type))
		}
		cursor.close()
		_place.value = resultList
	}
}