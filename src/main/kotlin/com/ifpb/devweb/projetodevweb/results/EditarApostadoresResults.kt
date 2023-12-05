package com.ifpb.devweb.projetodevweb.results


sealed interface EditarApostadoresResults : Result
data object EditarOkResult: EditarApostadoresResults
data object ApostadorNaoEncontradoResult: EditarApostadoresResults