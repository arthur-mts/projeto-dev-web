package com.ifpb.devweb.projetodevweb.model.dto

import com.ifpb.devweb.projetodevweb.domain.Concurso

data class ResultadoSorteioDTO(
        val concurso: Concurso,
        val vencedor: String?
)