package DTO;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequest(

    @NotBlank(message = "campo nome não pode ser vazio")
    String nome,
    @NotBlank(message = "campo descrição não pode ser vazio")
    String descricao,
    @NotBlank(message = "campo preço não pode ser vazio")
    BigDecimal preco,
    @NotBlank(message = "campo estoque não pode ser vazio")
    Integer estoque,
    @NotBlank(message = "campo categoria não pode ser vazio")
    String categoria,

    String imageUrl,
    boolean ativo

){}
