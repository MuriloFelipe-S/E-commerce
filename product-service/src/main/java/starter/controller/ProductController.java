package starter.controller;

import jakarta.validation.Valid;
import starter.DTO.ProductRequest;
import starter.DTO.ProductResponse;
import starter.DTO.PromotionRequest;
import starter.DTO.PromotionResponse;
import starter.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starter.enumCategorias.ProductCategory;
import starter.service.ProductService;

import java.net.URI;
import java.util.HashMap;
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
                product.getSubCategoria(),
                product.getImageUrl(),
                product.isAtivo()
        );
    }

    @PostMapping
    public ResponseEntity<ProductResponse> criarProduto(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.criarProduto(productRequest); // Cria um novo produto usando o serviço, passando o DTO de requisição
        return ResponseEntity.created(URI.create("/products/" + product.getId())) // retorna a URI do novo produto criado "URI" é um identificador de recurso uniforme, usado para identificar recursos na web
                .body(toResponse(product)); // Retorna o produto criado com o status 201 Created
    }

    @PostMapping("/criar-varios")
    public ResponseEntity<List<ProductResponse>> criarVarios(@RequestBody @Valid List<ProductRequest> productRequests) {
        List<Product> produtosCriados = productService.criarVariosProdutos(productRequests); // Cria vários produtos usando o serviço, passando a lista de DTOs de requisição

        List<ProductResponse> response = produtosCriados.stream() // Converte a lista de produtos criados em uma lista de ProductResponse
                .map(this::toResponse) // Mapeia cada produto para um ProductResponse
                .toList(); // Coleta os resultados em uma lista

        return ResponseEntity.ok(response); // Retorna a lista de produtos criados com o status 200 OK
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> buscarTodosProdutos() {
        List<Product> produtos = productService.buscarTodosProdutos(); // Busca todos os produtos usando o serviço

        List<ProductResponse> response = produtos.stream() // Converte a lista de produtos encontrados em uma lista de ProductResponse
                .map(this::toResponse) // Mapeia cada produto para um ProductResponse
                .toList(); // Coleta os resultados em uma lista

        return ResponseEntity.ok(response); // Retorna todos os produtos
    }

    @GetMapping("/search/{categoria}")
    public ResponseEntity<List<ProductResponse>> buscarProdutosPorCategoria(@PathVariable ProductCategory categoria) { // metodo para buscar produtos por categoria @PathVariable é usado para capturar o valor da variável de caminho na URL
        List<Product> produtos = productService.buscarPorCategoria(categoria); // Busca produtos por categoria usando o serviço

        List<ProductResponse> response = produtos.stream() // Converte a lista de produtos encontrados em uma lista de ProductResponse
                .map(ProductResponse::new) // Mapeia cada produto para um ProductResponse
                .toList(); // Coleta os resultados em uma lista

        return ResponseEntity.ok(response); // Retorna a lista de produtos encontrados
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarProdutoPorId(@PathVariable Long id) {
        ProductResponse productAtualizado = productService.buscarPorId(id); // Busca o produto pelo ID usando o serviço
        return ResponseEntity.ok(productAtualizado); // Retorna o produto encontrado
    }

    @GetMapping("/search/ativo")
    public ResponseEntity<List<ProductResponse>> buscarProdutosAtivos() {
        List<Product> produtosAtivos = productService.listarProdutosAtivos(); // Busca produtos ativos usando o serviço

        List<ProductResponse> response = produtosAtivos.stream() // Converte a lista de produtos ativos encontrados em uma lista de ProductResponse
                .map(this::toResponse) // Mapeia cada produto ativo para um ProductResponse
                .toList(); // Coleta os resultados em uma lista

        return ResponseEntity.ok(response); // Retorna a lista de produtos ativos encontrados
    }

    @GetMapping("/search/inativo")
    public ResponseEntity<List<ProductResponse>> buscarProdutosInativos() {
        List<Product> produtosInativos = productService.listarProdutosInativos();

        List<ProductResponse> response = produtosInativos.stream() // Converte a lista de produtos inativos encontrados em uma lista de ProductResponse
                .map(this::toResponse) // Mapeia cada produto inativo para um ProductResponse
                .toList(); // Coleta os resultados em uma lista

        return ResponseEntity.ok(response); // Retorna a lista de produtos inativos encontrados
    }

    @PutMapping("{id}/atualizar")
    public ResponseEntity<ProductResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ProductRequest productRequest) { // anotacoes @PathVariable e @RequestBody são usadas para capturar o ID do produto da URL, @Valid é usado para validar o DTO de requisição
        Product productAtualizado = productService.atualizarProduto(id, productRequest); // Atualiza o produto com os dados do DTO de requisição
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto atualizado
    }

    @PutMapping("{id}/aplicar-desconto")
    public ResponseEntity<PromotionResponse> aplicarDesconto(@PathVariable Long id, @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse DTOresponse = productService.aplicarDesconto(id, promotionRequest); // Aplica o desconto ao produto com o ID fornecido e retorna um DTO de resposta com os detalhes do desconto aplicado
        return ResponseEntity.ok(DTOresponse); // Retorna o desconto aplicado
    }

    @PatchMapping("{id}/atualizar-parcial")
    public ResponseEntity<ProductResponse> atualizarParcial(@PathVariable Long id, @RequestBody @Valid Map<String, Object> campos) { // @PatchMapping é usado para atualizar parcialmente o produto, Map<String, Object> permite receber apenas alguns campos do produto
        Product productAtualizado = productService.atualizarParcialmente(id, campos); // Atualiza parcialmente o produto com os dados do DTO de requisição
        return ResponseEntity.ok(toResponse(productAtualizado)); // Retorna o produto atualizado
    }

    @PatchMapping("{id}/estoque")
    public ResponseEntity<ProductResponse> atualizarEstoque(@PathVariable Long id, @RequestBody Integer estoque) {
        ProductResponse productAtualizado = productService.atualizarEstoque(id, estoque); // Atualiza o estoque do produto com o ID fornecido
        return ResponseEntity.ok(productAtualizado); // Retorna o produto atualizado
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        productService.deletarProduto(id); // Deleta o produto pelo ID usando o serviço
        return ResponseEntity.noContent().build(); // Retorna o status 204 No Content indicando que a operação foi bem-sucedida
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deletarVariosProdutos(@RequestBody List<Long> ids) {
        List<Long> idsNaoEncontrados = productService.deletarVariosProdutos(ids); // Busca IDs que não foram encontrados

        Map<String, Object> response = new HashMap<>(); // Cria um mapa para armazenar a resposta
        response.put("mensagem", "Produtos deletados com sucesso"); // Adiciona uma mensagem de sucesso ao mapa
        response.put("naoEncontrados", idsNaoEncontrados); // Adiciona os IDs não encontrados ao mapa

        return ResponseEntity.ok(response); // Retorna o status 204 No Content indicando que a operação foi bem-sucedida
    }


}