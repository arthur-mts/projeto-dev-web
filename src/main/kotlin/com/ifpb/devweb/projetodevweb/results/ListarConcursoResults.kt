package com.ifpb.devweb.projetodevweb.results

import com.ifpb.devweb.projetodevweb.domain.Concurso

sealed interface ListarConcursoResults : Result
data class ListarConcursoOkResult(val concursos: List<Concurso>) : ListarConcursoResults