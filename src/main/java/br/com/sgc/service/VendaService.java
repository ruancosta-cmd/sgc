package br.com.sgc.service;

import br.com.sgc.dto.VendaRequestDTO;
import br.com.sgc.model.*;
import br.com.sgc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Venda registrarVenda(VendaRequestDTO dto, Usuario usuarioLogado) {
        // 1. Validar se o cliente existe no banco de dados
        Cliente cliente = clienteRepository.findById(dto.clienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setUsuario(usuarioLogado);
        venda.setValorTotal(BigDecimal.ZERO);

        List<ItemVenda> itensVenda = new ArrayList<>();
        BigDecimal totalGeral = BigDecimal.ZERO;

        // 2. Processar cada produto vindo da lista do carrinho
        for (var itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado! ID: " + itemDto.produtoId()));

            // Regra de Negócio: Validar estoque suficiente
            if (produto.getQuantidadeEstoque() < itemDto.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome() 
                    + " (Disponível: " + produto.getQuantidadeEstoque() + ")");
            }

            // Regra de Negócio: Dar baixa automática no estoque do produto
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.quantidade());
            produtoRepository.save(produto);

            // 3. Montar e estruturar o Item da Venda
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProduto(produto); 
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(produto.getPreco());

            // Calcular o subtotal acumulado do item
            BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade()));
            totalGeral = totalGeral.add(subTotal);

            itensVenda.add(item);
        }

        // Regra de Negócio: Atribuir valor total calculado automaticamente
        venda.setValorTotal(totalGeral);
        venda.setItens(itensVenda);

        return vendaRepository.save(venda);
    }
}