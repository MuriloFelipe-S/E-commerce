package controller;

import DTO.ProductRequest;
import DTO.ProductResponse;
import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injetando o serviço de produtos

    private ProductResponse toResponse(Product product) { //metodo para converter o produto em um DTO de resposta
        return new ProductResponse( //Cria um novo ProductResponse com os dados do produto
                product.getId(),
                product.getNome(),
                product.getDescricao(),
                product.getPreco(),
                product.getEstoque(),
                product.getCategoria(),
                product.getImageUrl(),
                product.isAtivo()
        );
    }

    @PostMapping
    public ResponseEntity<ProductResponse> criarProduto(@RequestBody @PathVariable ProductRequest productRequest) {
        Product product = productService.criarProduto(productRequest); //
        return ResponseEntity.created(URI.create("/api/products/" + product.getId())) // retorna a URI do novo produto criado "URI" é um identificador de recurso uniforme, usado para identificar recursos na web
                .body(toResponse(product)); // Retorna o produto criado com o status 201 Created
    }

    @GetMapping
    public ResponseEntity<List<Product>> buscarTodosProdutos() {
         return ResponseEntity.ok(productService.buscarTodosProdutos()); // Retorna todos os produtos
    }

    @GetMapping("/search/{categoria}")
    public ResponseEntity <List<Product>> buscarProdutosPorCategoria(@PathVariable String categoria) { // metodo para buscar produtos por categoria @PathVariable é usado para capturar o valor da variável de caminho na URL
        List<Product> produtos = productService.buscarPorCategoria(categoria); // Busca produtos por categoria usando o serviço
        return ResponseEntity.ok(produtos); // Retorna a lista de produtos encontrados
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarProdutoPorId(@PathVariable Long id) {
        Product productAtualizado = productService.buscarProdutoPorId(id); // Busca o produto pelo ID usando o serviço
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto encontrado
    }
}