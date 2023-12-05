package com.ifpb.devweb.projetodevweb.domain

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate
import java.util.UUID

data class Concurso(
        val id: UUID,
        val nome: String,
        @ColumnName("data_sorteio")
        val dataSorteio: LocalDate,
        @ColumnName("numero_sorteado")
        val numeroSorteado: Int?,
        val status: Status,
) {
    enum class Status {
        EM_ABERTO,
        CONCLUIDO,
        CANCELADO,
    }
}