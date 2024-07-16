package campus.tech.kakao.map

data class Location(
    val title: String,
    val address: String,
    val category: String
){
    companion object {
        fun toLocation(locationDto: LocationDto): Location {
            return Location(locationDto.place_name, locationDto.address_name, locationDto.category_group_name)
        }
    }
}