package campus.tech.kakao.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ViewModelFactory {
    class LocationViewModelFactory(
        private val locationDbAccessor: LocationDbAccessor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(locationDbAccessor) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    class SavedLocationViewModelFactory(
        private val locationDbAccessor: LocationDbAccessor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(SavedLocationViewModel::class.java)) {
                return SavedLocationViewModel(locationDbAccessor) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
