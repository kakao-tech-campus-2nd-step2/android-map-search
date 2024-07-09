package campus.tech.kakao.map.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Mapper.EntityToModelMapper
import campus.tech.kakao.map.Model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val favoriteDao: FavoriteDao,
    private val retrofitService: RetrofitService,
    private val docToPlaceMapper: EntityToModelMapper<Document, Place>
) : PlaceRepository {
    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    override val currentResult: LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<MutableList<Place>> = MutableLiveData()
    override val favoritePlace: LiveData<MutableList<Place>> = _favoritePlace

    init {
        getCurrentFavorite()
    }

    override fun getCurrentFavorite() {
        _favoritePlace.value = favoriteDao.getCurrentFavorite()
    }

    override fun getSimilarPlacesByName(name: String) {
        _currentResult.value = placeDao.getSimilarPlacesByName(name)
    }

    override fun getPlaceByName(name: String): Place {
        return placeDao.getPlaceByName(name)
    }

    override fun addFavorite(name: String) {
        val place = currentResult.value?.find {
            it.name == name
        } ?: Place("Error")
        favoriteDao.addFavorite(place)

        val favorites = _favoritePlace.value ?: mutableListOf<Place>()
        favorites.add(place)
        _favoritePlace.value = favorites
    }

    override fun deleteFavorite(name: String) {
        favoriteDao.deleteFavorite(name)
        getCurrentFavorite()
    }

    override suspend fun searchPlaceRemote(name: String) {
        _currentResult.postValue(getPlaceByNameRemote(name))
    }

    private suspend fun getPlaceByNameRemote(name: String): List<Place> =
        withContext(Dispatchers.IO) {
            val pageCount = getPageCount(name)
            var placeList: MutableList<Place> = mutableListOf<Place>()

            for (page in 1..pageCount) {
                val req = retrofitService.requestProducts(query = name, page = page).execute()

                when (req.code()) {
                    200 -> {
                        req.body()?.documents?.forEach {
                            placeList.add(docToPlaceMapper.map(it))
                        }
                    }
                    else -> {}
                }

            }
            return@withContext placeList
        }

    private fun getPageCount(name: String): Int {
        val req = retrofitService.requestProducts(query = name).execute()

        when (req.code()) {
            200 -> return minOf(
                MAX_PAGE, req.body()?.meta?.pageable_count ?: 1
            )

            else -> return 0
        }
    }

    companion object {
        const val MAX_PAGE = 2
    }
}