package DTO;

import java.math.BigDecimal;

public record ProductResponse( // DTO para representar a resposta do produto
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    String categoria,
    String imageUrl,
    boolean ativo

){}
