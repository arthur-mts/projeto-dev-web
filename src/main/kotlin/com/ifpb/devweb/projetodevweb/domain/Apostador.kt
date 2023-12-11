package com.ifpb.devweb.projetodevweb.domain

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate
import java.util.UUID

data class Apostador(
        val id: UUID,
        val nome: String,
        val email: String?,
        @ColumnName("data_nascimento")
        val dataDeNascimento: LocalDate,
        val status: Status,
) {
    enum class Status {
        ATIVO,
        DELETADO
    }
}