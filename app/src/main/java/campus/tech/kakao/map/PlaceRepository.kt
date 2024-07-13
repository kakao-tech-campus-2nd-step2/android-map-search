package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.getIntOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)
    private var placeList = mutableListOf<Place>()

    fun insertPlace(place: Place) {
        dbHelper.writableDatabase.use {
            val values = ContentValues().apply {
                put(MyPlaceContract.Place.COLUMN_IMG, place.img)
                put(MyPlaceContract.Place.COLUMN_NAME, place.name)
                put(MyPlaceContract.Place.COLUMN_CATEGORY, place.category.category)
                put(MyPlaceContract.Place.COLUMN_LOCATION, place.location)
            }

            it.insert(MyPlaceContract.Place.TABLE_NAME, null, values)
        }
    }

    fun insertLog(place: Place) {
        dbHelper.writableDatabase.use { db ->
            db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(BaseColumns._ID),
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category),
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category.category)
                    }
                    db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                }
            }
        }
    }

    fun reset() {
        dbHelper.writableDatabase.use {
            it.execSQL("DELETE FROM ${MyPlaceContract.Place.TABLE_NAME}")
        }
    }

    fun insertInitialData(): MutableList<Place> {
        //kakao에서 데이터 가져와서 place 객체 생성하기
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
        RetrofitObject.retrofitService.getPlace(apiKey, "CE7")
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents
                        documentList?.forEach {
                            val place = Place(img = R.drawable.cafe, name = it.placeName, location = it.addressName, category = PlaceCategory.CAFE)
                            placeList.add(place)
                            insertPlace(place)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("KakaoAPI", "Error: $errorBody")
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                }
            })

        RetrofitObject.retrofitService.getPlace(apiKey, "PM9")
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents
                        documentList?.forEach {
                            val place = Place(img = R.drawable.hospital, name = it.placeName, location = it.addressName, category = PlaceCategory.PHARMACY)
                            placeList.add(place)
                            insertPlace(place)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("KakaoAPI", "Error: $errorBody")
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                }
            })
        return placeList
    }

    fun hasResearchEntries() : Boolean {
        dbHelper.readableDatabase.use {
            it.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null).use { cursor ->
                return if (cursor.moveToFirst()) {
                    val count = cursor.getIntOrNull(0) ?: 0
                    count > 0
                } else {
                    false
                }
            }
        }
    }

    fun getResearchEntries(): List<Place> {
        val researchList = mutableListOf<Place>()

        dbHelper.readableDatabase.use { db ->
            db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY
                ),
                null, null, null, null, null
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val categoryDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val category = PlaceCategory.fromCategory(categoryDisplayName)
                    val place = Place(img = img, name = name, location = location, category = category)
                    researchList.add(place)
                }
            }
        }

        return researchList
    }

    fun deleteResearchEntry(place: Place) {
        dbHelper.writableDatabase.use {
            it.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category)
            )
        }
    }

    object RetrofitObject {
        val retrofitService: KakaoApiService by lazy { Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)
        }
    }
}