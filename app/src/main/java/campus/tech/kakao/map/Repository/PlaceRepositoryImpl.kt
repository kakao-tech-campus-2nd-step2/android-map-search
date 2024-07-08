package campus.tech.kakao.map.Repository

import android.util.Log
import campus.tech.kakao.map.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.PlaceCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val favoriteDao: FavoriteDao,
    private val retrofitService: RetrofitService
) : PlaceRepository {
    override fun getCurrentFavorite(): MutableList<Place> {
        return favoriteDao.getCurrentFavorite()
    }

    override fun getSimilarPlacesByName(name: String): List<Place>? {
        return placeDao.getSimilarPlacesByName(name)
    }

    override fun getPlaceByName(name: String): Place {
        return placeDao.getPlaceByName(name)
    }

    override fun addFavorite(name: String) {
        favoriteDao.addFavorite(getPlaceByName(name))
    }

    override fun deleteFavorite(name: String) {
        favoriteDao.deleteFavorite(name)
    }

    private suspend fun retro(name: String): List<Document>? = withContext(Dispatchers.IO) {
        val req = retrofitService.requestProducts(query = name).execute()
        when (req.code()) {
            200 -> {
                return@withContext req.body()?.documents
            }

            else -> return@withContext listOf<Document>()
        }
    }

}