package starter.controller;

import jakarta.validation.Valid;
import starter.DTO.ProductRequest;
import starter.DTO.ProductResponse;
import starter.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starter.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injetando o serviço de produtos

    @GetMapping("/test")
    public ResponseEntity<String> testandoAPI() {
        return ResponseEntity.ok("API de Produtos está funcionando!"); // Retorna uma mensagem de teste para verificar se a API está funcionando
    }

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
    public ResponseEntity<ProductResponse> criarProduto(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.criarProduto(productRequest); //
        return ResponseEntity.created(URI.create("/api/products/" + product.getId())) // retorna a URI do novo produto criado "URI" é um identificador de recurso uniforme, usado para identificar recursos na web
                .body(toResponse(product)); // Retorna o produto criado com o status 201 Created
    }

    @GetMapping
    public ResponseEntity<List<Product>> buscarTodosProdutos() {
        return ResponseEntity.ok(productService.buscarTodosProdutos()); // Retorna todos os produtos
    }

    @GetMapping("/search/{categoria}")
    public ResponseEntity<List<Product>> buscarProdutosPorCategoria(@PathVariable String categoria) { // metodo para buscar produtos por categoria @PathVariable é usado para capturar o valor da variável de caminho na URL
        List<Product> produtos = productService.buscarPorCategoria(categoria); // Busca produtos por categoria usando o serviço
        return ResponseEntity.ok(produtos); // Retorna a lista de produtos encontrados
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarProdutoPorId(@PathVariable Long id) {
        Product productAtualizado = productService.buscarProdutoPorId(id); // Busca o produto pelo ID usando o serviço
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto encontrado
    }

    @GetMapping("/search/ativo")
    public ResponseEntity<List<Product>> buscarProdutosAtivos() {
        List<Product> produtosAtivos = productService.listarProdutosAtivos(); // Busca produtos ativos usando o serviço
        return ResponseEntity.ok(produtosAtivos); // Retorna a lista de produtos ativos encontrados
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ProductRequest productRequest) { // anotacoes @PathVariable e @RequestBody são usadas para capturar o ID do produto da URL, @Valid é usado para validar o DTO de requisição
        Product productAtualizado = productService.atualizarProduto(id, productRequest); // Atualiza o produto com os dados do DTO de requisição
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto atualizado
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductResponse> atualizarParcial(@PathVariable Long id, @RequestBody @Valid Map<String, Object> campos) { // @PatchMapping é usado para atualizar parcialmente o produto, Map<String, Object> permite receber apenas alguns campos do produto
        Product productAtualizado = productService.atualizarParcialmente(id, campos); // Atualiza parcialmente o produto com os dados do DTO de requisição
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto atualizado
    }
}