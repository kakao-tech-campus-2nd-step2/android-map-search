package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.Datasource.Local.Dao.FavoriteDaoImpl
import campus.tech.kakao.map.Datasource.Local.Dao.PlaceDaoImpl
import campus.tech.kakao.map.Datasource.Local.SqliteDB
import campus.tech.kakao.map.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Mapper.DocToPlaceMapper
import campus.tech.kakao.map.Model.PlaceContract
import campus.tech.kakao.map.Repository.PlaceRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val sqliteDB = SqliteDB(context, PlaceContract.DATABASE_NAME, null, 1)
    private val placeDaoImpl = PlaceDaoImpl(sqliteDB.writableDatabase)
    private val favoriteDao = FavoriteDaoImpl(sqliteDB.writableDatabase)
    private val retrofitService = Retrofit.Builder()
        .baseUrl(RetrofitService.BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)
    val repository = PlaceRepositoryImpl(
        placeDaoImpl, favoriteDao, retrofitService,
        DocToPlaceMapper()
    )
}