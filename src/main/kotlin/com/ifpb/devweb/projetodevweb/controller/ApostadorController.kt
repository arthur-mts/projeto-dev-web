package com.ifpb.devweb.projetodevweb.controller

import com.ifpb.devweb.projetodevweb.domain.Aposta
import com.ifpb.devweb.projetodevweb.domain.Apostador
import com.ifpb.devweb.projetodevweb.model.dto.ApostarDTO
import com.ifpb.devweb.projetodevweb.model.dto.CriarApostadorDTO
import com.ifpb.devweb.projetodevweb.model.dto.EditarApostadorDTO
import com.ifpb.devweb.projetodevweb.results.*
import com.ifpb.devweb.projetodevweb.service.ApostaService
import com.ifpb.devweb.projetodevweb.service.ApostadorService
import com.ifpb.devweb.projetodevweb.service.SorteioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/apostador")
class ApostadorController(private val apostadorService: ApostadorService, private val apostaService: ApostaService) {
    @PostMapping
    fun criarApostador(@RequestBody body: CriarApostadorDTO): ResponseEntity<*> {
        return with(body) {
            val entity = Apostador(
                    id = UUID.randomUUID(),
                    nome = nome,
                    email = email,
                    dataDeNascimento = dataDeNascimento,
                    status = Apostador.Status.ATIVO,
            )
            when (apostadorService.criarApostador(entity)) {
                is EmailJaExistenteResult -> ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário com esse email já cadastrado")
                is ApostadorCriadoResult -> ResponseEntity.status(HttpStatus.CREATED).body(entity)
            }
        }
    }

    @GetMapping
    fun listarApostadores(): ResponseEntity<*> {
        return when (val result = apostadorService.listarApostador()) {
            is ListarOkResult -> ResponseEntity.status(HttpStatus.OK).body(result.apostadores)
        }
    }

    @PutMapping("/{id}")
    fun editarApostador(@PathVariable("id") id: UUID, @RequestBody body: EditarApostadorDTO): ResponseEntity<*> {
        return when (apostadorService.editarApostador(body.nome, body.email, id)) {
            is EditarOkResult -> ResponseEntity<Unit>(HttpStatus.OK)
            is ApostadorNaoEncontradoResult -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apostador \"$id\" não encontrado")
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteMapping(@PathVariable("id") id: UUID): ResponseEntity<*> {
        return when(apostadorService.deletarApostador(id)) {
            EditarOkResult -> ResponseEntity<Unit>(HttpStatus.OK)
            ApostadorNaoEncontradoResult -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apostador \"$id\" não encontrado")
        }
    }

    @PostMapping("/{id}/apostar/{idConcurso}")
    fun apostar(@PathVariable("id") idApostador: UUID,
                @PathVariable("idConcurso") idConcurso: UUID,
                @RequestBody body: ApostarDTO): ResponseEntity<*> {

        if (!SorteioService.numeroEValidoParaAposta(body.numero)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numero invalido")
        }

        val entity = Aposta(
                id = UUID.randomUUID(),
                idConcurso = idConcurso,
                idApostador = idApostador,
                dataAposta = LocalDate.now(),
                numeroApostado = body.numero,
                status = Aposta.Status.EM_ABERTO,
        )

        return when (apostaService.registrarAposta(entity)) {
            is ApostarOkResult -> ResponseEntity.status(HttpStatus.OK).body(entity)
            is ApostarApostadorNaoEncontradoResult -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apostador \"$idApostador\" não encontrado")
            is ApostarConcursoNaoEncontradoResult -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Concurso \"$idConcurso\" não encontrado")
            is ApostarConcursoInvalidoResult -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Concurso \"$idConcurso\" invalido")
            is ApostarNumeroJaEscolhido -> ResponseEntity.status(HttpStatus.FORBIDDEN).body("Número \"${body.numero}\" ja escolhido")
        }
    }
    @GetMapping("/{id}/apostas")
    @ResponseStatus(HttpStatus.OK)
    fun listarApostas(@PathVariable("id") idApostador: UUID): List<Aposta> {
        return apostaService.listarApostasPorApostador(idApostador)
    }
}