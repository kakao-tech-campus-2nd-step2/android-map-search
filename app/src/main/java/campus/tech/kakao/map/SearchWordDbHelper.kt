package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.SearchWordContract.DB_VERSION

class SearchWordDbHelper(context: Context): SQLiteOpenHelper(
	context, SearchWordContract.DB_NAME, null, DB_VERSION) {
	private val _searchWords = MutableLiveData<List<SearchWord>>()
	val searchSameSelection = "${SearchWordContract.COLUMN_NAME_NAME} = ? AND " +
			"${SearchWordContract.COLUMN_NAME_ADDRESS} = ? AND " +
			"${SearchWordContract.COLUMN_NAME_TYPE} = ?"
	override fun onCreate(db: SQLiteDatabase?) {
		createTable(db)
	}

	private fun createTable(db: SQLiteDatabase?) {
		db?.execSQL(
			"CREATE TABLE ${SearchWordContract.TABLE_NAME} " +
					"(${SearchWordContract.COLUMN_NAME_NAME} TEXT, " +
					"${SearchWordContract.COLUMN_NAME_ADDRESS} TEXT, " +
					"${SearchWordContract.COLUMN_NAME_TYPE} TEXT)"
		)
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		db?.execSQL("DROP TABLE IF EXISTS ${SearchWordContract.TABLE_NAME}")
		createTable(db)
	}

	fun getSearchWords(): MutableLiveData<List<SearchWord>> {
		return _searchWords
	}

	fun addWord(word: SearchWord) {
		val db = writableDatabase
		if (existWord(word, db)){
			deleteWord(word)
		}
		val values = ContentValues()
		values.put(SearchWordContract.COLUMN_NAME_NAME, word.name)
		values.put(SearchWordContract.COLUMN_NAME_ADDRESS, word.address)
		values.put(SearchWordContract.COLUMN_NAME_TYPE, word.type)
		db.insert(SearchWordContract.TABLE_NAME, null, values)
		db.close()
		updateSearchWords()
	}

	fun existData(): Boolean{
		val db = readableDatabase
		val cursor = db.query(
			SearchWordContract.TABLE_NAME,
			arrayOf(SearchWordContract.COLUMN_NAME_NAME),
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

	fun existWord(word: SearchWord, db: SQLiteDatabase): Boolean{
		val selection = searchSameSelection
		val cursor = db.query(
			SearchWordContract.TABLE_NAME,
			arrayOf(SearchWordContract.COLUMN_NAME_NAME),
			selection,
			arrayOf(word.name, word.address, word.type),
			null,
			null,
			"${SearchWordContract.COLUMN_NAME_NAME} DESC"
		)

		val result = cursor.moveToFirst()
		cursor.close()
		return if (result) true else false
	}

	fun deleteWord(word: SearchWord){
		val db = writableDatabase
		val selection = searchSameSelection
		val selectionArgs = arrayOf(word.name, word.address, word.type)
		db.delete(SearchWordContract.TABLE_NAME, selection, selectionArgs)
		updateSearchWords()
	}

	fun updateSearchWords(){
		val db = readableDatabase
		val resultList = mutableListOf<SearchWord>()
		val cursor = db.query(
			SearchWordContract.TABLE_NAME,
			arrayOf(SearchWordContract.COLUMN_NAME_NAME, SearchWordContract.COLUMN_NAME_ADDRESS, SearchWordContract.COLUMN_NAME_TYPE),
			null,
			null,
			null,
			null,
			null
		)
		while (cursor.moveToNext()) {
			val name = cursor.getString(
				cursor.getColumnIndexOrThrow(SearchWordContract.COLUMN_NAME_NAME)
			)
			val address = cursor.getString(
				cursor.getColumnIndexOrThrow(SearchWordContract.COLUMN_NAME_ADDRESS)
			)
			val type = cursor.getString(
				cursor.getColumnIndexOrThrow(SearchWordContract.COLUMN_NAME_TYPE))
			resultList.add(SearchWord(name, address, type))
		}
		cursor.close()
		_searchWords.value = resultList
	}
}