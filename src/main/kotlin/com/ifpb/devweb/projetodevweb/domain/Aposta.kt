package com.ifpb.devweb.projetodevweb.domain

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate
import java.util.UUID

data class Aposta(
        val id: UUID,
        @ColumnName("id_apostador")
        val idApostador: UUID,
        @ColumnName("id_concurso")
        val idConcurso: UUID,
        @ColumnName("data_aposta")
        val dataAposta: LocalDate,
        @ColumnName("numero_apostado")
        val numeroApostado: Int,
        val status: Status,
        @ColumnName("nome_apostador")
        val nomeApostador: String? = null
) {
    enum class Status {
        EM_ABERTO,
        GANHOU,
        PERDEU,
        CANCELADA,
    }
}