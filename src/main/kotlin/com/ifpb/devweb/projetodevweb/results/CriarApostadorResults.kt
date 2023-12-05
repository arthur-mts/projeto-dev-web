package com.ifpb.devweb.projetodevweb.results

sealed interface CriarApostadorResults: Result

data object ApostadorCriadoResult: CriarApostadorResults

data object EmailJaExistenteResult: CriarApostadorResults
