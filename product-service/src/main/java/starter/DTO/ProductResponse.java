package starter.DTO;

import starter.entity.Product;
import starter.enumCategorias.ProductCategory;
import starter.enumCategorias.SubCategory;

import java.math.BigDecimal;

public record ProductResponse( // DTO para representar a resposta do produto

    // DTO é um objeto de transferência de dados, usado para transferir dados entre camadas da aplicação
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    ProductCategory categoria,
    SubCategory subCategory,
    String imageUrl,
    boolean ativo

){
    public ProductResponse(Product product) { // Construtor que recebe um Product e inicializa o DTO
        this(
            product.getId(),
            product.getNome(),
            product.getDescricao(),
            product.getPreco(),
            product.getEstoque(),
            product.getCategoria(),
            product.getSubCategoria(),
            product.getImageUrl(),
            product.isAtivo()
        );
    }
}
