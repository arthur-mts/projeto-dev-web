package com.ifpb.devweb.projetodevweb.service

import com.ifpb.devweb.projetodevweb.domain.Concurso
import com.ifpb.devweb.projetodevweb.results.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ConcursoService(private val jdbi: Jdbi, private val sorteioService: SorteioService) {
    fun encontrarConcurso(id: UUID): Concurso? {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select * from concurso where id = :id").bind("id", id).mapTo<Concurso>().firstOrNull()
        }
    }

    fun criarConcurso(concurso: Concurso): CriarConcursoResults {
        return jdbi.withHandleUnchecked { handle ->
            val statement = handle.createUpdate("insert into concurso(id, nome, data_sorteio, status) values (:id, :nome, :dataSorteio, :status)")
            statement.bindBean(concurso)
            statement.execute()
            CriarConcursoOkResult
        }
    }

    fun listarConcursos(status: Concurso.Status?): ListarConcursoResults {
        return jdbi.withHandleUnchecked { handle ->
            var query = "select * from concurso"
            if (status != null) {
                query += " where status = :status"
            }

            val queryStatement = handle.createQuery(query)
            status?.let { queryStatement.bind("status", it.name) }
            val concursos = queryStatement.mapTo<Concurso>().list()
            ListarConcursoOkResult(
                    concursos = concursos
            )
        }
    }

    @Transactional
    fun sortearConcurso(id: UUID): SortearConcursoResults {

        val concurso = encontrarConcurso(id) ?: return ConcursoNaoEncontradoResult
        if (concurso.status != Concurso.Status.EM_ABERTO) {
            return ConcursoInvalido
        }

        val (idVencedor, numeroSorteado) = sorteioService.executaSorteio(id)

        jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("update concurso set numero_sorteado = :numeroSorteado, status = :status where id = :id")
                    .bind("id", id)
                    .bind("numeroSorteado", numeroSorteado)
                    .bind("status", Concurso.Status.CONCLUIDO.name)
                    .execute()
        }

        return SorteioOkResult(
                concurso.copy(numeroSorteado = numeroSorteado, status = Concurso.Status.CONCLUIDO),
                idVencedor
        )
    }

    @Transactional
    fun cancelarConcurso(id: UUID) {
        jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("update concurso set status = :status where id = :id")
                    .bind("id", id)
                    .bind("status", Concurso.Status.CANCELADO)
                    .execute()
        }
        sorteioService.cancelaSorteio(id)
    }
}