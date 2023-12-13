package com.ifpb.devweb.projetodevweb.repository

import com.ifpb.devweb.projetodevweb.domain.Aposta
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ApostaRepository(private val jdbi: Jdbi) {
    fun cancelarApostasPorConcurso(idConcurso: UUID) {
        jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("update apostas set status = :status where id_concurso = :idConcurso")
                    .bind("idConcurso", idConcurso)
                    .bind("status", Aposta.Status.CANCELADA)
                    .execute()
        }
    }
    fun listarApostasPorConcurso(idConcurso: UUID): List<Aposta> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select ap.*, apo.nome as nome_apostador, c.nome as nome_concurso from apostas ap join apostadores apo on apo.id = ap.id_apostador join concurso c on c.id = ap.id_concurso " +
                    "where ap.id_concurso = :idConcurso").bind("idConcurso", idConcurso).mapTo<Aposta>().toList()
        }
    }

    fun editarStatusDeApostas(statusPorAposta: Map<UUID, Boolean>) {
        jdbi.withHandleUnchecked { handle ->
            val batchStatement = handle.prepareBatch("update apostas set status = :status where id = :id")
            statusPorAposta.forEach { (id, venceu) ->
                batchStatement.add(
                        mapOf(
                                "status" to if (venceu) Aposta.Status.GANHOU else Aposta.Status.PERDEU,
                                "id" to id
                        )
                )
            }
            batchStatement.execute()
        }
    }

    fun listarApostasPorApostador(idApostador: UUID): List<Aposta> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select ap.*, apo.nome as nome_apostador, c.nome as nome_concurso from apostas ap join apostadores apo on apo.id = ap.id_apostador join concurso c on c.id = ap.id_concurso " +
                    "where ap.id_apostador = :idApostador").bind("idApostador", idApostador).mapTo<Aposta>().toList()
        }
    }

    fun salvarAposta(aposta: Aposta) {
        jdbi.withHandleUnchecked { handle ->
            val statement = handle
                    .createUpdate(
                            """
                            insert into apostas(id, id_apostador, numero_apostado, data_aposta, id_concurso, status)
                            values (:id, :idApostador, :numeroApostado, :dataAposta, :idConcurso, :status)
                            """.trimIndent()
                    )
            statement.bindBean(aposta)
            statement.execute()
        }
    }

    fun listarApostas(status: Aposta.Status?): List<Aposta> {
        return jdbi.withHandleUnchecked { handle ->
            var query = "select ap.*, apo.nome as nome_apostador, c.nome as nome_concurso from apostas ap join apostadores apo on apo.id = ap.id_apostador join concurso c on c.id = ap.id_concurso"

            if (status != null) {
                query += " where status = :status"
            }
            val statement = handle.createQuery(query)
            if (status != null) {
                statement.bind("status", status)
            }
            statement.mapTo<Aposta>().toList()
        }
    }
}