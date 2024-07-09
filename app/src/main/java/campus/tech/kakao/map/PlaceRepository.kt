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
        val db = dbHelper.writableDatabase

        try {
            val values = ContentValues().apply {
                put(MyPlaceContract.Place.COLUMN_IMG, place.img)
                put(MyPlaceContract.Place.COLUMN_NAME, place.name)
                put(MyPlaceContract.Place.COLUMN_CATEGORY, place.category.category)
                put(MyPlaceContract.Place.COLUMN_LOCATION, place.location)
            }

            db.insert(MyPlaceContract.Place.TABLE_NAME, null, values)
        } finally {
            db.close()
        }
    }

    fun insertLog(place: Place) {
        val db = dbHelper.writableDatabase

        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(BaseColumns._ID),
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category),
                null,
                null,
                null
            )

            try {
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category.category)
                    }

                    val newRowId = db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                    if (newRowId == -1L) {
                        Log.e("PlaceRepository", "Failed to insert row for ${place.name}")
                    } else {
                        Log.d("PlaceRepository", "Successfully inserted row for ${place.name}")
                    }
                }
            } finally {
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error inserting row: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun reset() {
        val db = dbHelper.writableDatabase
        try {
            db.execSQL("DELETE FROM ${MyPlaceContract.Place.TABLE_NAME}")
            //db.execSQL("DELETE FROM ${MyPlaceContract.Research.TABLE_NAME}")
        } finally {
            db.close()
        }
    }

    fun insertInitialData() {
        //kakao에서 데이터 가져와서 place 객체 생성하기
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
        val retrofitService = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)

        retrofitService.getPlace(apiKey, "CE7")
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents
                        documentList?.forEach {
                            val place = Place(R.drawable.cafe, it.place_name, it.address_name, PlaceCategory.CAFE)
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

        retrofitService.getPlace(apiKey, "PM9")
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents
                        documentList?.forEach {git ad
                            val place = Place(R.drawable.hospital, it.place_name, it.address_name, PlaceCategory.PHARMACY)
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
    }

    fun returnPlaceList() = placeList

    fun hasResearchEntries() : Boolean {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null)
            try {
                return if (cursor.moveToFirst()) {
                    val count = cursor.getIntOrNull(0)
                    count!! > 0
                } else {
                    false
                }
            } finally {
                cursor.close()
            }
        } finally {
            db.close()
        }
    }

    fun getResearchEntries(): List<Place> {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY
                ),
                null, null, null, null, null
            )

            val researchList = mutableListOf<Place>()
            try {
                while (cursor.moveToNext()) {
                    val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val categoryDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val category = PlaceCategory.fromCategory(categoryDisplayName)
                    val place = Place(img, name, location, category)
                    researchList.add(place)
                }
            } finally {
                cursor.close()
            }
            return researchList
        } finally {
            db.close()
        }
    }


    fun deleteResearchEntry(place: Place) {
        val db = dbHelper.writableDatabase
        try {
            db.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category)
            )
            Log.d("PlaceRepository", "Successfully deleted row for ${place.name}")
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error deleting row: ${e.message}")
        } finally {
            db.close()
        }
    }

}