package com.ifpb.devweb.projetodevweb.service

import com.ifpb.devweb.projetodevweb.domain.ExecucaoSorteio
import com.ifpb.devweb.projetodevweb.repository.ApostaRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.UUID

@Service
class SorteioService(private val apostaRepository: ApostaRepository) {
    companion object {
        const val NUMERO_MINIMO_PARA_CONCURSO = 1
        const val NUMERO_MAXIMO_PARA_CONCURSO = 100
        fun numeroEValidoParaAposta(numero: Int) = numero in NUMERO_MINIMO_PARA_CONCURSO..NUMERO_MAXIMO_PARA_CONCURSO
    }

    fun cancelaSorteio(idConcurso: UUID) {
        apostaRepository.cancelarApostasPorConcurso(idConcurso)
    }

    fun executaSorteio(idConcurso: UUID): ExecucaoSorteio {
        val numeroSorteado = SecureRandom().nextInt(NUMERO_MINIMO_PARA_CONCURSO, NUMERO_MAXIMO_PARA_CONCURSO)

        val apostas = apostaRepository.listarApostasPorConcurso(idConcurso)
        val apostasVencidasEPerdidas = apostas.associate {
            Pair(it.id, it.numeroApostado == numeroSorteado)
        }
        apostaRepository.editarStatusDeApostas(apostasVencidasEPerdidas)
        val idVencedor = apostasVencidasEPerdidas.filter { it.value }.keys.firstOrNull()
        return ExecucaoSorteio(
                idVencedor,
                numeroSorteado
        )
    }
}