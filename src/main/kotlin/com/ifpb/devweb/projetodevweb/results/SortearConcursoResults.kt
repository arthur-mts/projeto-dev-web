package com.ifpb.devweb.projetodevweb.results

import com.ifpb.devweb.projetodevweb.domain.Concurso
import java.util.UUID

sealed interface SortearConcursoResults : Result

data object ConcursoJaSorteadoResult : SortearConcursoResults
data object ConcursoNaoEncontradoResult : SortearConcursoResults
data class SorteioOkResult(val concurso: Concurso, val idVencedor: UUID?) : SortearConcursoResults
//data object EmailJaExistenteResult: CriarApostadorResults
