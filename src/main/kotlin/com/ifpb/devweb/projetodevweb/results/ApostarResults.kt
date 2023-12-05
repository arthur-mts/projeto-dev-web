package com.ifpb.devweb.projetodevweb.results

sealed interface ApostarResults : Result
data object ApostarApostadorNaoEncontradoResult : ApostarResults
data object ApostarConcursoNaoEncontradoResult : ApostarResults
data object ApostarConcursoJaRodouResult : ApostarResults
data object ApostarNumeroJaEscolhido : ApostarResults
data object ApostarOkResult: ApostarResults