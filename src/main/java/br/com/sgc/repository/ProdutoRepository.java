package br.com.sgc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sgc.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}