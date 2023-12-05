package com.ifpb.devweb.projetodevweb.controller

import com.ifpb.devweb.projetodevweb.domain.Aposta
import com.ifpb.devweb.projetodevweb.domain.Concurso
import com.ifpb.devweb.projetodevweb.model.dto.CriarConcursoDTO
import com.ifpb.devweb.projetodevweb.model.dto.ResultadoSorteioDTO
import com.ifpb.devweb.projetodevweb.results.*
import com.ifpb.devweb.projetodevweb.service.ApostaService
import com.ifpb.devweb.projetodevweb.service.ApostadorService
import com.ifpb.devweb.projetodevweb.service.ConcursoService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/concurso")
class ConcursoController(
        private val concursoService: ConcursoService,
        private val apostaService: ApostaService,
        private val apostadorService: ApostadorService,
) {
    @PostMapping
    fun criarConcurso(@RequestBody criarConcursoDTO: CriarConcursoDTO): ResponseEntity<*> {
        if (criarConcursoDTO.dataSorteio < LocalDate.now()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data do sorteio inválida")
        }
        return with(criarConcursoDTO) {
            val entity = Concurso(
                    id = UUID.randomUUID(),
                    nome = nome,
                    dataSorteio = dataSorteio,
                    numeroSorteado = null,
                    status = Concurso.Status.EM_ABERTO,
            )
            when (concursoService.criarConcurso(entity)) {
                CriarConcursoOkResult -> ResponseEntity.status(HttpStatus.CREATED).body(entity)
            }
        }
    }

    @GetMapping
    fun listarConcursos(@RequestParam("status", required = false) status: Concurso.Status?): ResponseEntity<*> {
        return when (val result = concursoService.listarConcursos(status)) {
            is ListarConcursoOkResult -> ResponseEntity.status(HttpStatus.OK).body(result.concursos)
        }
    }

    @PatchMapping("/{id}/sortear")
    fun sortearConcurso(@PathVariable("id") id: UUID): ResponseEntity<*> {
        return when (val result = concursoService.sortearConcurso(id)) {
            is ConcursoNaoEncontradoResult -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Concurso não encontrado")
            is SorteioOkResult -> {
                val nomeVencedor = result.idVencedor?.let { apostadorService.encontrarApostador(it) }?.nome
                ResponseEntity.status(HttpStatus.OK).body(ResultadoSorteioDTO(
                        concurso = result.concurso,
                        vencedor = nomeVencedor,
                ))
            }
            is ConcursoJaSorteadoResult -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Concurso já foi sorteado")
        }
    }

    @GetMapping("/{id}/apostas")
    @ResponseStatus(HttpStatus.OK)
    fun listarApostas(@PathVariable("id") id: UUID): List<Aposta> {
        return apostaService.listarApostasPorConcurso(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun cancelarConcurso(@PathVariable("id") id: UUID) {
        concursoService.cancelarConcurso(id)
    }
}