package com.ifpb.devweb.projetodevweb.domain

import java.util.UUID

data class ExecucaoSorteio(
        val vencedor: UUID?,
        val numeroSorteado: Int,
)