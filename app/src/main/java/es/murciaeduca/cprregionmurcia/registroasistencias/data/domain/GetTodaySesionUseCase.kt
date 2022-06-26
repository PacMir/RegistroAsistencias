package es.murciaeduca.cprregionmurcia.registroasistencias.data.domain

import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository

class GetTodaySesionUseCase(private val sesionRepository: SesionRepository) {

    suspend operator fun invoke(email: String): Unit = sesionRepository.getAll(email)

}