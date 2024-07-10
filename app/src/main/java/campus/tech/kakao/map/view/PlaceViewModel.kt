package campus.tech.kakao.map.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.use_case.GetLogsUseCase
import campus.tech.kakao.map.domain.use_case.GetPlacesUseCase
import campus.tech.kakao.map.domain.use_case.AddLogUseCase
import campus.tech.kakao.map.domain.use_case.RemoveLogUseCase
import campus.tech.kakao.map.domain.use_case.UpdatePlacesUseCase

class PlaceViewModel(
    private val getPlacesUseCase: GetPlacesUseCase,
    private val getLogsUseCase: GetLogsUseCase,
    private val updatePlacesUseCase: UpdatePlacesUseCase,
    private val addLogUseCase: AddLogUseCase,
    private val removeLogUseCase: RemoveLogUseCase
): ViewModel(){
    private val _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> get() = _logList

    val searchText = MutableLiveData<String>()

    fun clearSearch() {
        searchText.value = ""
    }
    fun getPlaces(placeName: String): List<Place>{
        return getPlacesUseCase.invoke(placeName)
    }

    fun updatePlaces(places: List<Place>){
        updatePlacesUseCase.invoke(places)
    }

    fun getLogs(): List<Place>{
        return getLogsUseCase.invoke()
    }

    fun addLog(place: Place){
        val updatedList = _logList.value?.toMutableList() ?: mutableListOf()
        val existingLog = updatedList.find { it.id == place.id }
        if (existingLog != null) {
            updatedList.remove(existingLog)
            updatedList.add(0, existingLog)
        } else {
            addLogUseCase(place)
            updatedList.add(0, place)
        }
        _logList.value = updatedList
    }

    fun removeLog(id: String){
        removeLogUseCase.invoke(id)
        _logList.value = getLogs()
    }

    companion object {
        fun provideFactory(application: PlaceApplication): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlaceViewModel(
                            application.getPlacesUseCase,
                            application.getLogsUseCase,
                            application.updatePlacesUseCase,
                            application.addLogUseCase,
                            application.removeLogUseCase
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}