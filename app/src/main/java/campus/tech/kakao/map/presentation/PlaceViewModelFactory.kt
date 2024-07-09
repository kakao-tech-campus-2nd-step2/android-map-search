package campus.tech.kakao.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceViewModelFactory(private val placeRepository: PlaceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // modelClass 에 MainActivityViewModel이 상속되었는지?
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(placeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
