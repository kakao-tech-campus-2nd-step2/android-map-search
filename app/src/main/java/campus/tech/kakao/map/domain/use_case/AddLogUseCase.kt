package campus.tech.kakao.map.domain.use_case

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class AddLogUseCase(
    private val placeRepository: PlaceRepository
){
    operator fun invoke(placeLog: Place){
        return placeRepository.addLog(placeLog)
    }
}