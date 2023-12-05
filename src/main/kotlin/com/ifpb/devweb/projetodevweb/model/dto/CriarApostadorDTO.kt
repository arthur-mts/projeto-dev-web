package com.ifpb.devweb.projetodevweb.model.dto

import java.time.LocalDate

data class CriarApostadorDTO(
    val nome: String,
    val email: String?,
    val dataDeNascimento: LocalDate
)