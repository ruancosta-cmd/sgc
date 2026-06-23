package br.com.sgc.dto;

public record ItemVendaRequestDTO(
    Long produtoId,
    Integer quantidade
) {}