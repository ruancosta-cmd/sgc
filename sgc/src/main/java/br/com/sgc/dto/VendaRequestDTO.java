package br.com.sgc.dto;

import java.util.List;

public record VendaRequestDTO(
    Long clienteId,
    List<ItemVendaRequestDTO> itens
) {}