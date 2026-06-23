package br.com.sgc.controller;

import br.com.sgc.dto.VendaRequestDTO;
import br.com.sgc.model.Venda;
import br.com.sgc.model.Usuario;
import br.com.sgc.service.VendaService;
import br.com.sgc.repository.VendaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaRepository vendaRepository; // Injeção que estava faltando para sumir o erro!

    @PostMapping
    public ResponseEntity<Venda> criarVenda(
            @RequestBody @Valid VendaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Venda novaVenda = vendaService.registrarVenda(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }

    @GetMapping("/faturamento")
    public ResponseEntity<BigDecimal> obterFaturamentoTotal() {
        BigDecimal total = vendaRepository.somarTotalFaturado();
        return ResponseEntity.ok(total);
    }
}