package com.ifpb.devweb.projetodevweb.controller

import com.ifpb.devweb.projetodevweb.domain.Aposta
import com.ifpb.devweb.projetodevweb.service.ApostaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/apostas")
class ApostaController(private val apostaService: ApostaService) {


    @GetMapping
    fun listarApostas(@RequestParam(required = false) status: Aposta.Status?) = apostaService.listarApostas(status)

}