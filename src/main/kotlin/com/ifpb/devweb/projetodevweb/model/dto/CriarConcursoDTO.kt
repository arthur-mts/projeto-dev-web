package com.ifpb.devweb.projetodevweb.model.dto

import java.time.LocalDate

data class CriarConcursoDTO(
        val nome: String,
        val dataSorteio: LocalDate,
)