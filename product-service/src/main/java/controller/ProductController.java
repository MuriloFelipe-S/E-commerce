package controller;

import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.ProductService;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injetando o serviço de produtos

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
    public ResponseEntity<Product> buscarProdutoPorId(@PathVariable Long id) {
        Product product = productService.buscarProdutoPorId(id); // Busca o produto pelo ID usando o serviço
        return ResponseEntity.ok(product); // Retorna o produto encontrado
    }
}