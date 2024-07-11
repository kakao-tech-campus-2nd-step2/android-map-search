package campus.tech.kakao.map.domain.usecase

interface GetSearchHistoryUseCase {
    operator fun invoke(): Set<String>
}