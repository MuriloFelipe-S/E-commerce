package starter.controller;

import jakarta.validation.Valid;
import starter.DTO.*;
import starter.Mapper.ProductMapper;
import starter.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import static starter.Mapper.ProductMapper.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starter.enumCategorias.ProductCategory;
import starter.service.ProductService;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injetando o serviço de produtos

    private List<ProductResponse> toResponseList(List<Product> products){
        return products.stream().map(product -> ProductMapper.toResponse(product, productService.calcularPrecoComDesconto(product))).toList(); // Converte uma lista de produtos em uma lista de ProductResponse
    }

    //Métodos GET

    @GetMapping("/test")
    public ResponseEntity<String> testandoAPI() {
        return ResponseEntity.ok("API de Produtos está funcionando!"); // Retorna uma mensagem de teste para verificar se a API está funcionando
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> buscarTodosProdutos() {
        List<Product> produtos = productService.buscarTodosProdutos(); // Busca todos os produtos usando o serviço
        return ResponseEntity.ok(toResponseList(produtos)); // Retorna todos os produtos
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductResponse>> buscarProdutosPorCategoria(@PathVariable ProductCategory categoria) { // metodo para buscar produtos por categoria @PathVariable é usado para capturar o valor da variável de caminho na URL
        List<Product> produtos = productService.buscarPorCategoria(categoria); // Busca produtos por categoria usando o serviço
        return ResponseEntity.ok(toResponseList(produtos)); // Retorna a lista de produtos encontrados
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarProdutoPorId(@PathVariable Long id) {
        Product product = productService.buscarProdutoPorId(id); // Busca o produto pelo ID usando o serviço
        BigDecimal precoComDesconto = productService.calcularPrecoComDesconto(product); // Calcula o preço com desconto
        return ResponseEntity.ok(toResponse(product, precoComDesconto)); // Retorna o produto encontrado
    }

    @GetMapping("/ativo")
    public ResponseEntity<List<ProductResponse>> buscarProdutosAtivos() {
        List<Product> produtosAtivos = productService.listarProdutosAtivos(); // Busca produtos ativos usando o serviço
        return ResponseEntity.ok(toResponseList(produtosAtivos)); // Retorna a lista de produtos ativos encontrados
    }

    @GetMapping("/inativo")
    public ResponseEntity<List<ProductResponse>> buscarProdutosInativos() {
        List<Product> produtosInativos = productService.listarProdutosInativos();
        return ResponseEntity.ok(toResponseList(produtosInativos)); // Retorna a lista de produtos inativos encontrados
    }

    //Métodos POST

    @PostMapping
    public ResponseEntity<ProductResponse> criarProduto(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.criarProduto(productRequest); // Cria um novo produto usando o serviço, passando o DTO de requisição
        ProductResponse response = toResponse(product, product.getPreco());
        return ResponseEntity.created(URI.create("/products/"+ product.getId())) // Retorna o produto criado com o status 201 Created e a URI do novo recurso;
                .body(response); // Retorna o produto criado
    }

    @PostMapping("/criar-varios")
    public ResponseEntity<List<ProductResponse>> criarVarios(@RequestBody @Valid List<ProductRequest> productRequests) {
        List<Product> produtosCriados = productService.criarVariosProdutos(productRequests); // Cria vários produtos usando o serviço, passando a lista de DTOs de requisição
        return ResponseEntity.ok(toResponseList(produtosCriados)); // Retorna a lista de produtos criados com o status 200 OK
    }

    //Métodos PUT e PATCH

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ProductRequest productRequest) { // anotacoes @PathVariable e @RequestBody são usadas para capturar o ID do produto da URL, @Valid é usado para validar o DTO de requisição
        Product productAtualizado = productService.atualizarProduto(id, productRequest); // Atualiza o produto com os dados do DTO de requisição
        BigDecimal precoComDesconto = productService.calcularPrecoComDesconto(productAtualizado); // Calcula o preço com desconto
        return ResponseEntity.ok(toResponse(productAtualizado, precoComDesconto)); // Retorna o produto atualizado
    }

    @PutMapping("/{id}/aplicar-desconto")
    public ResponseEntity<PromotionResponse> aplicarDesconto(@PathVariable Long id, @RequestBody @Valid PromotionRequest promotionRequest) {
        Product produtoComDesconto = productService.aplicarDesconto(id, promotionRequest);
        BigDecimal precoComDesconto = productService.calcularPrecoComDesconto(produtoComDesconto); // Calcula o preço com desconto

        return ResponseEntity.ok(toPromotionResponse(produtoComDesconto, precoComDesconto)); // Retorna o desconto aplicado
    }

    @PatchMapping("/{id}/atualizar-parcial")
    public ResponseEntity<ProductResponse> atualizarParcial(@PathVariable Long id, @RequestBody @Valid Map<String, Object> campos) { // @PatchMapping é usado para atualizar parcialmente o produto, Map<String, Object> permite receber apenas alguns campos do produto
        Product productAtualizado = productService.atualizarParcialmente(id, campos); // Atualiza parcialmente o produto com os dados do DTO de requisição
        BigDecimal precoComDesconto = productService.calcularPrecoComDesconto(productAtualizado); // Calcula o preço com desconto
        return ResponseEntity.ok(toResponse(productAtualizado, precoComDesconto)); // Retorna o produto atualizado
    }

    @PatchMapping("/{id}/estoque")
    public ResponseEntity<ProductResponse> atualizarEstoque(@PathVariable Long id, @RequestBody @Valid EstoqueRequest estoqueRequest) {
        Product productAtualizado = productService.atualizarEstoque(id, estoqueRequest.estoque()); // Atualiza o estoque do produto com o ID fornecido
        BigDecimal precoComDesconto = productService.calcularPrecoComDesconto(productAtualizado); // Calcula o preço com desconto
        return ResponseEntity.ok(toResponse(productAtualizado, precoComDesconto)); // Retorna o produto atualizado
    }

    //Métodos DELETE

    @DeleteMapping("/{id}")
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

        return ResponseEntity.ok(response);
    }


}