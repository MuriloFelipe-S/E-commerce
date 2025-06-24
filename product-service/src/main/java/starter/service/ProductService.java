package starter.service;


import org.apache.coyote.BadRequestException;
import starter.DTO.ProductRequest;
import starter.entity.Product;
import starter.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starter.repository.ProductRepository;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
        productRepository.deleteById(id); //deleta o produto do banco de dados pelo ID
    }

    public List<Product> buscarPorCategoria(String categoria) { //metodo para buscar produtos por categoria
        return productRepository.findByCategoria(categoria); //retorna os produtos da categoria especificada
    }

    public List<Product> listarProdutosAtivos() { //metodo para listar produtos ativos
        return productRepository.findAll().stream() //busca todos os produtos e filtra apenas os ativos
                .filter(Product::isAtivo) //filtra os produtos que estão ativos
                .toList(); //converte o resultado para uma lista
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


}
