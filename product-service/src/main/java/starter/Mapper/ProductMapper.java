package starter.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import starter.DTO.ProductRequest;
import starter.DTO.ProductResponse;
import starter.DTO.PromotionResponse;
import starter.entity.Product;
import starter.service.ProductService;

import java.math.BigDecimal;

public class ProductMapper {


    @Autowired
    private ProductService productService;

    public static ProductResponse toResponse(Product product, BigDecimal precoComDesconto) {
        return new ProductResponse(
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

    public static Product toEntity(ProductRequest productRequest) {
        return new Product(
                productRequest.nome(),
                productRequest.descricao(),
                productRequest.preco(),
                productRequest.estoque(),
                productRequest.categoria(),
                productRequest.subCategoria(),
                productRequest.imageUrl(),
                productRequest.ativo()
        );
    }

    public static PromotionResponse toPromotionResponse(Product product, BigDecimal precoComDesconto) {
        return new PromotionResponse(
                product.getPreco(),
                precoComDesconto,
                product.getDesconto(),
                product.getDataInicio(),
                product.getDataFim()
        );
    }

}
