package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.use_case.GetLogsUseCase
import campus.tech.kakao.map.domain.use_case.GetPlacesUseCase
import campus.tech.kakao.map.domain.use_case.UpdateLogsUseCase
import campus.tech.kakao.map.domain.use_case.RemoveLogUseCase
import campus.tech.kakao.map.domain.use_case.UpdatePlacesUseCase

class PlaceApplication: Application() {

    private val placeRepository: PlaceRepository by lazy { PlaceRepositoryImpl.getInstance(this) }

    val getLogsUseCase by lazy { GetLogsUseCase(placeRepository) }
    val getPlacesUseCase by lazy { GetPlacesUseCase(placeRepository)}
    val updateLogsUseCase by lazy { UpdateLogsUseCase(placeRepository) }
    val updatePlacesUseCase by lazy { UpdatePlacesUseCase(placeRepository) }
    val removeLogUseCase by lazy {RemoveLogUseCase(placeRepository)}
}