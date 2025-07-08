package starter.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import starter.enumCategorias.ProductCategory;
import starter.enumCategorias.SubCategory;


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

    @NotNull(message = "campo categoria não pode ser vazio")
    ProductCategory categoria,

    @NotNull(message = "campo subcategoria não pode ser vazio")
    SubCategory subCategoria,

    String imageUrl,

    boolean ativo

){}
