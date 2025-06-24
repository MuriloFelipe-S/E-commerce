package starter.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

public record ProductRequest( // DTO para representar a requisição de criação ou atualização de um produto

    @NotBlank(message = "campo nome não pode ser vazio")
    String nome,

    @NotBlank(message = "campo descrição não pode ser vazio")
    String descricao,

    @NotNull(message = "campo preço não pode ser vazio")
    BigDecimal preco,

    @NotNull(message = "campo estoque não pode ser vazio")
    Integer estoque,

    @NotBlank(message = "campo categoria não pode ser vazio")
    String categoria,

    String imageUrl,

    boolean ativo

){}
