package starter.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import starter.DTO.ProductRequest;
import starter.DTO.ProductResponse;
import starter.DTO.PromotionRequest;
import starter.DTO.PromotionResponse;
import starter.entity.Product;
import starter.enumCategorias.ProductCategory;
import starter.enumCategorias.SubCategory;
import starter.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starter.repository.ProductRepository;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import org.springframework.util.ReflectionUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; //injetando o repositório de produtos

    public Product criarProduto(ProductRequest request) { //metodo para criar um produto
        validarPreco(request.preco());
        Product produto = new Product();
        preencherProduct(produto, request); //preenche os dados do produto com os dados do DTO de requisição

        return productRepository.save(produto); //salva o produto no banco de dados e retorna o produto salvo
    }

    public List<Product> criarVariosProdutos(List<ProductRequest> productRequests) { //metodo para criar varios produtos de uma so vez
        return productRequests.stream() //cria um fluxo de ProductRequest a partir da lista de ProductRequest
                .map(this::criarProduto)//para cada ProductRequest, chama o metodo criarProduto para criar um Product
                .collect(Collectors.toList()); //mapeia cada ProductRequest para um Product e coleta em uma lista
    }

    public Product buscarProdutoPorId(Long id) { //metodo para buscar um produto por ID
        return productRepository.findById(id) //busca o produto pelo ID no repositório
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o ID " + id + " não foi encontrado.")); //retorna o produto se encontrado, caso contrário lança uma exceção
    }

    public List<Product> buscarTodosProdutos() { //metodo para buscar todos os produtos
        return productRepository.findAll(); //retorna todos os produtos do banco de dados
    }

    public void validarPreco(BigDecimal preco) { //metodo para validar o preço do produto
        if (preco.compareTo(BigDecimal.ZERO) <= 0) { //verifica se o preço é nulo ou menor ou igual a zero
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero"); //lança uma exceção se o preço for inválido
        }
    }

    public void preencherProduct(Product product, ProductRequest productRequest) { //metodo para preencher os dados do produto com os dados do DTO de requisição
        product.setNome(productRequest.nome());
        product.setDescricao(productRequest.descricao());
        product.setPreco(productRequest.preco());
        product.setEstoque(productRequest.estoque());
        product.setCategoria(productRequest.categoria());
        product.setImageUrl(productRequest.imageUrl());
        product.setAtivo(productRequest.ativo());
    }

    public Product atualizarProduto(Long id, ProductRequest productRequest) { //metodo para atualizar um produto
        validarPreco(productRequest.preco()); //valida o preço do produto
        Product productoExistente = buscarProdutoPorId(id); //busca o produto existente pelo ID
        preencherProduct(productoExistente, productRequest); //preenche os dados do produto existente com os dados do DTO de requisição
        return productRepository.save(productoExistente); //salva o produto atualizado no banco de dados e retorna o produto atualizado
    }

    public void deletarProduto(Long id) { //metodo para deletar um produto
        buscarProdutoPorId(id); //busca o produto pelo ID para verificar se ele existe
        productRepository.deleteById(id); //deleta o produto do banco de dados pelo ID se existir
        log.info("Produto com o ID {} foi deletado com sucesso.", id); //loga a mensagem de sucesso no console
    }

    public List<Long> deletarVariosProdutos(List<Long> ids) { //metodo para deletar varios produtos
        List<Product> produtos = productRepository.findAllById(ids); //busca todos os produtos pelos IDs

        List<Long> encontrados = produtos.stream()//filtra os produtos encontrados
                        .map(Product::getId)//pega o ID de cada produto encontrado
                        .toList();//converte o resultado para uma lista de IDs encontrados

        List<Long> naoEncontrados = ids.stream()//filtra os IDs que não foram encontrados
                        .filter(id -> !encontrados.contains(id))// verifica se o ID não está na lista de IDs encontrados
                        .toList();// converte o resultado para uma lista de IDs não encontrados

        if (!naoEncontrados.isEmpty()) {// verifica se existem IDs não encontrados
            System.out.println("Os seguintes IDs não foram encontrados: {"+ naoEncontrados+"}");// imprime os IDs não encontrados no console
        }

        productRepository.deleteAllInBatch(produtos);// deleta todos os produtos encontrados em lote
        return naoEncontrados;// retorna a lista de IDs não encontrados
    }

    public List<Product> buscarPorCategoria(ProductCategory categoria) { //metodo para buscar produtos por categoria
        return productRepository.findByCategoria(categoria); //retorna os produtos da categoria especificada
    }

    public List<SubCategory> ListarSubCategorias(ProductCategory category){
        return Arrays.stream(SubCategory.values())
                .filter(sc -> sc.getCategoria() == category) //filtra as subcategorias que pertencem à categoria especificada
                .toList();
    }

    public List<Product> listarProdutosAtivos() { //metodo para listar produtos ativos
        return productRepository.findByAtivoTrue(); //retorna os produtos ativos do banco de dados
    }

    public Product atualizarParcialmente(Long id, Map<String, Object> campos) {

        Product produto = buscarProdutoPorId(id); //buscando produto pelo ID
        boolean alterado = false; //criando uma variável para verificar se houve alteração

        Set<String> camposValidos = Set.of("nome", "descricao", "preco", "estoque", "categoria", "imageUrl", "ativo"); //definindo os campos válidos que podem ser atualizados

        for (Map.Entry<String, Object> entry : campos.entrySet()) { //iterando sobre os campos a serem atualizados
            String nomeDoCampo = entry.getKey();

            if (!camposValidos.contains(nomeDoCampo)) {
                continue;
            }

            Field field = ReflectionUtils.findField(Product.class, entry.getKey()); //mapeando o nome do campo para o objeto Field correspondente na classe Product
            if (field != null) { //verificando se o campo existe
                field.setAccessible(true); //tornando o campo acessivel para escrita

                Object novoValor = entry.getValue(); //pegando o novo valor do campo

                if ("preco".equals(nomeDoCampo) && novoValor != null) {
                    novoValor = new BigDecimal(novoValor.toString()); //convertendo o novo valor para BigDecimal se o campo for preço
                    validarPreco((BigDecimal) novoValor); //validando o novo valor do preço
                }

                Object valorAtual = ReflectionUtils.getField(field, produto); //pegando o valor atual do campo no produto

                if ((valorAtual == null && novoValor != null) || //verificando se o valor atual é nulo e o novo valor não é nulo
                        (valorAtual != null && !valorAtual.equals(novoValor))) { //ou se o valor atual é diferente do novo valor
                    ReflectionUtils.setField(field, produto, novoValor);//atualizando o campo com o novo valor
                    alterado = true; //marcando que uma alteração foi feita
                }
            }
        }

        if (!alterado) { //verificando se realmente houve alguma alteração
            throw new RuntimeException("Nenhum campo foi alterado"); //lançando uma exceção se nenhum campo foi alterado
        }

        return productRepository.save(produto); //salvando o produto atualizado no banco e retornando o produto atualizado

    }

    public ProductResponse atualizarEstoque(Long id, Integer estoque) { //metodo para atualizar o estoque de um produto
        if (estoque < 0){
            throw new IllegalArgumentException("Estoque nao pode ser menor que zero"); //verifica se o estoque é negativo
        }
        Product produto = buscarProdutoPorId(id); //busca o produto pelo ID
        produto.setEstoque(estoque);
        productRepository.save(produto); //atualiza o estoque do produto no banco de dados
        return new ProductResponse(produto); //atualiza o estoque do produto e retorna o produto atualizado como ProductResponse
    }

    public List<Product> listarProdutosInativos() { //metodo para listar produtos inativos
        return productRepository.findByAtivoFalse(); //retorna os produtos inativos do banco de dados
    }

    public PromotionResponse DTOresponse(Product product) {
        BigDecimal precoComDesconto = calcularPrecoComDesconto(product); //calcula o preço com desconto

        return new PromotionResponse( //cria um novo objeto PromotionResponse com os dados do produto e o preço com desconto
                product.getPreco(),
                precoComDesconto,
                product.getDesconto(),
                product.getDataInicio(),
                product.getDataFim()
        );
    }

    public BigDecimal calcularPrecoComDesconto(Product product) {
        if (product.getDesconto() == null || product.getDataInicio() == null || product.getDataFim() == null) {
            return product.getPreco();
        }

        LocalDate hoje = LocalDate.now(); //pega a data atual
        if (hoje.isBefore(product.getDataInicio()) || hoje.isAfter(product.getDataFim())) { //verifica se a data atual está fora do período da promoção
            return product.getPreco(); //se estiver fora do período, retorna o preço original
        }

        BigDecimal valorDesconto = product.getPreco() //calcula o valor do desconto
                .multiply(product.getDesconto())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); //divide o desconto por 100 e arredonda para duas casas decimais

        return product.getPreco().subtract(valorDesconto); //subtrai o valor do desconto do preço original e retorna o preço com desconto
    }

    public void aplicarDesconto(Long id, PromotionRequest promotionRequest) { //metodo para aplicar desconto em produtos
        Product produto = buscarProdutoPorId(id); //busca o produto pelo ID

        produto.setDesconto(promotionRequest.desconto()); //define o desconto no produto
        produto.setDataInicio(promotionRequest.dataInicio()); //define a data de início da promoção
        produto.setDataFim(promotionRequest.dataFim()); //define a data de fim da promoção

        productRepository.save(produto); //salva o produto atualizado no banco de dados

    }

}
