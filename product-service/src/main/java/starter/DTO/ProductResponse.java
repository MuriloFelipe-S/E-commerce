package starter.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import starter.entity.Product;
import starter.enumCategorias.ProductCategory;
import starter.enumCategorias.SubCategory;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL) // Inclui apenas campos não nulos na serialização JSON
public record ProductResponse( // DTO para representar a resposta do produto

    // DTO é um objeto de transferência de dados, usado para transferir dados entre camadas da aplicação
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    BigDecimal desconto,
    BigDecimal precoComDesconto,
    Integer estoque,
    ProductCategory categoria,
    SubCategory subCategory,
    String imageUrl,
    boolean ativo

){
    public ProductResponse(Product product, BigDecimal precoComDesconto) { // Construtor que recebe um Product e inicializa o DTO
        this(
            product.getId(),
            product.getNome(),
            product.getDescricao(),
            product.getPreco(),
            product.getDesconto(),
            precoComDesconto,
            product.getEstoque(),
            product.getCategoria(),
            product.getSubCategoria(),
            product.getImageUrl(),
            product.isAtivo()
        );
    }
}
