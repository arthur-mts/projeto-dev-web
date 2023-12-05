package com.ifpb.devweb.projetodevweb.service

import com.ifpb.devweb.projetodevweb.domain.Aposta
import com.ifpb.devweb.projetodevweb.repository.ApostaRepository
import com.ifpb.devweb.projetodevweb.results.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ApostaService(
        private val apostadorService: ApostadorService,
        private val concursoService: ConcursoService,
        private val apostaRepository: ApostaRepository,
) {

    fun listarApostasPorConcurso(idConcurso: UUID) = apostaRepository.listarApostasPorConcurso(idConcurso)
    fun listarApostasPorApostador(idApostador: UUID) = apostaRepository.listarApostasPorApostador(idApostador)

    fun registrarAposta(aposta: Aposta): ApostarResults {
        if (!apostadorService.apostadorExiste(aposta.idApostador)) {
            return ApostarApostadorNaoEncontradoResult
        }

        val concurso = concursoService.encontrarConcurso(aposta.idConcurso) ?: return ApostarConcursoNaoEncontradoResult

        if (concurso.numeroSorteado != null) {
            return ApostarConcursoJaRodouResult
        }

        val apostasJaFeitas = apostaRepository.listarApostasPorConcurso(concurso.id)

        if (apostasJaFeitas.find { it.numeroApostado == aposta.numeroApostado } != null) {
            return ApostarNumeroJaEscolhido
        }

        apostaRepository.salvarAposta(aposta)
        return ApostarOkResult
    }
}