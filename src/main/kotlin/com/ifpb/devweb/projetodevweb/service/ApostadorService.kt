package com.ifpb.devweb.projetodevweb.service

import com.ifpb.devweb.projetodevweb.domain.Apostador
import com.ifpb.devweb.projetodevweb.results.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ApostadorService(
        private val jdbi: Jdbi,
) {
    fun encontrarApostador(id: UUID): Apostador? {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select * from apostador where id = :id").bind("id", id).mapTo<Apostador>().firstOrNull()
        }
    }

    fun apostadorExiste(id: UUID): Boolean {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select exists(select * from apostadores where id = :id)").bind("id", id).mapTo(Int::class.java).first() > 0
        }
    }

    fun criarApostador(apostador: Apostador): CriarApostadorResults {
        return jdbi.withHandleUnchecked { handle ->
            apostador.email?.let {
                val exists = handle.createQuery("select exists(select * from apostadores where email = :email)").bind("email", it).mapTo(Int::class.java).first() > 0
                if (exists) {
                    return@withHandleUnchecked EmailJaExistenteResult
                }
            }

            val statement = handle.createUpdate("insert into apostadores(id, nome, email, data_nascimento, status) values (:id, :nome, :email, :dataDeNascimento, 'ATIVO')")
            statement.bindBean(apostador)
            statement.execute()
            return@withHandleUnchecked ApostadorCriadoResult
        }
    }

    fun listarApostador(): ListarApostadoresResults {
        return jdbi.withHandleUnchecked { handle ->
            ListarOkResult(
                    handle.createQuery("select * from apostadores where status <> 'DELETADO'").mapTo(Apostador::class.java).list()
            )
        }
    }

    fun editarApostador(nome: String, email: String, id: UUID): EditarApostadoresResults {
        return jdbi.withHandleUnchecked { handle ->
            val statement = handle.createUpdate("update apostadores set nome = :nome, email = :email where id = :id and status <> 'DELETADO'")
            statement.bind("nome", nome)
            statement.bind("email", email)
            statement.bind("id", id)
            if (statement.execute() > 0) {
                EditarOkResult
            } else {
                ApostadorNaoEncontradoResult
            }
        }
    }

    fun deletarApostador(id: UUID): EditarApostadoresResults {
        return jdbi.withHandleUnchecked { handle ->
            val statement = handle.createUpdate("update apostadores set status = 'DELETADO' where id = :id and status <> 'DELETADO'")
            statement.bind("id", id)
            if (statement.execute() > 0) {
                EditarOkResult
            } else {
                ApostadorNaoEncontradoResult
            }
        }
    }

}