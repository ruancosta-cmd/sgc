package br.com.sgc.controller;

import br.com.sgc.model.Produto;
import br.com.sgc.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    // Listar todos os produtos (GET)
    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    // Salvar um novo produto (POST)
    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return repository.save(produto);
    }
}