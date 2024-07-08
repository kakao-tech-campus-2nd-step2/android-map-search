package campus.tech.kakao.map.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.PlaceCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val favoriteDao: FavoriteDao,
    private val retrofitService: RetrofitService
) : PlaceRepository {
    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    override val currentResult : LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<MutableList<Place>> = MutableLiveData()
    override val favoritePlace : LiveData<MutableList<Place>> = _favoritePlace

    init{
        getCurrentFavorite()
    }

    override fun getCurrentFavorite(){
        _favoritePlace.value = favoriteDao.getCurrentFavorite()
    }

    override fun getSimilarPlacesByName(name: String){
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

    private suspend fun requestDocumentByName(name: String): List<Document>? = withContext(Dispatchers.IO) {
        val req = retrofitService.requestProducts(query = name).execute()
        when (req.code()) {
            200 -> {
                return@withContext req.body()?.documents
            }
            else -> return@withContext listOf<Document>()
        }
    }

    override suspend fun searchPlaceRemote(name: String){
        val documents = requestDocumentByName(name)
        val res = mutableListOf<Place>()

        if (documents != null) {
            for (doc in documents) {
                res.add(
                    Place(
                        doc.place_name,
                        doc.address_name,
                        PlaceCategory.PHARMACY
                    )
                )
            }
        }

        _currentResult.postValue(res)
    }


}