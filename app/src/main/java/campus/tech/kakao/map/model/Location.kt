package campus.tech.kakao.map.model

data class Location(
    val title: String,
    val address: String,
    val category: String
){
    companion object {
        fun toLocation(locationDto: LocationDto): Location {
            return Location(locationDto.title, locationDto.address, locationDto.category)
        }
    }
}