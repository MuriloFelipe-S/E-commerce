package DTO;

import java.math.BigDecimal;

public record ProductResponse( // DTO para representar a resposta do produto

    // DTO é um objeto de transferência de dados, usado para transferir dados entre camadas da aplicação
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    String categoria,
    String imageUrl,
    boolean ativo

){}
