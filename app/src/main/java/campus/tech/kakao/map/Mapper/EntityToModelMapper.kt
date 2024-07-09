package campus.tech.kakao.map.Mapper

interface EntityToModelMapper<Document,Place> {
    fun map(document : Document) : Place
}