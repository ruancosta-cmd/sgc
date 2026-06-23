package br.com.sgc.repository;

import br.com.sgc.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    // Consulta customizada para somar todo o faturamento do sistema
    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v")
    BigDecimal somarTotalFaturado();
}