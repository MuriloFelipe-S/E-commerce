package service;


import DTO.ProductRequest;
import entity.Product;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProductRepository;
import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.util.ReflectionUtils;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository; //injetando o repositório de produtos

    public ProductService(ProductRepository productRepository) { //construtor da classe que recebe o repositório de produtos
        this.productRepository = productRepository; //inicializa o repositório de produtos
    }

    public Product criarProduto(ProductRequest request){ //metodo para criar um produto
        if (request.preco().compareTo(BigDecimal.ZERO) <= 0) { //verifica se o preço do produto é menor ou igual a zero
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero"); //lança uma exceção se o preço for inválido
        }

        Product produto = new Product(); //convertendo o DTO de requisição para uma entidade Product para salvar no banco de dados
        produto.setNome(request.nome()); //seta o nome do produto
        produto.setDescricao(request.descricao()); //seta a descrição do produto
        produto.setPreco(request.preco()); //seta o preço do produto
        produto.setEstoque(request.estoque()); //seta o estoque do produto
        produto.setCategoria(request.categoria()); //seta a categoria do produto
        produto.setImageUrl(request.imageUrl()); //seta a URL da imagem do produto
        produto.setAtivo(request.ativo()); //seta a disponibilidade do produto

        return productRepository.save(produto); //salva o produto no banco de dados e retorna o produto salvo

    }

    public Product buscarProdutoPorId(Long id) { //metodo para buscar um produto por ID
        return productRepository.findById(id) //busca o produto pelo ID no repositório
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o ID " + id + " não foi encontrado.")); //retorna o produto se encontrado, caso contrário lança uma exceção
    }

    public List<Product> buscarTodosProdutos() { //metodo para buscar todos os produtos
        return productRepository.findAll(); //retorna todos os produtos do banco de dados
    }

    public Product atualizarProduto(Long id, Product product) { //metodo para atualizar um produto
        if (!productRepository.existsById(id)) { //verifica se o produto existe pelo ID
            throw new RuntimeException("Produto não encontrado com o ID: " + id); //lança uma exceção se o produto não existir
        }
         //se existir, busca o produto existente pelo ID e atualiza seus campos
        Product produtoExistente = buscarProdutoPorId(id); //busca o produto existente pelo ID
        produtoExistente.setNome(product.getNome()); //atualiza o nome do produto
        produtoExistente.setDescricao(product.getDescricao()); //atualiza a descrição do produto
        produtoExistente.setPreco(product.getPreco()); //atualiza o preço do produto
        produtoExistente.setEstoque(product.getEstoque()); //atualiza o estoque do produto
        produtoExistente.setCategoria(product.getCategoria()); //atualiza a categoria do produto
        produtoExistente.setImageUrl(product.getImageUrl()); //atualiza a URL da imagem do produto
        produtoExistente.setAtivo(product.isAtivo()); //atualiza a disponibilidade do produto
        return productRepository.save(produtoExistente); //atualiza o produto no banco de dados e retorna o produto atualizado

    }

    public void deletarProduto(Long id) { //metodo para deletar um produto
        productRepository.deleteById(id); //deleta o produto do banco de dados pelo ID
    }

    public List<Product> buscarPorCategoria(String categoria) { //metodo para buscar produtos por categoria
        return productRepository.findByCategoria(categoria); //retorna os produtos da categoria especificada
    }

    public List<Product> listarProdutosAtivos() { //metodo para listar produtos ativos}
        return productRepository.findAll().stream() //busca todos os produtos e filtra apenas os ativos
                .filter(Product::isAtivo) //filtra os produtos que estão ativos
                .toList(); //converte o resultado para uma lista
    }

    public Product atualizarParcialmente(Long id, Map<String, Object> campos) {
        Product produto = buscarProdutoPorId(id); //buscando produto pelo ID
        boolean alterado = false; //criando uma variável para verificar se houve alteração

        for (Map.Entry<String, Object> entry : campos.entrySet()) { //iterando sobre os campos a serem atualizados
            Field field = ReflectionUtils.findField(Product.class, entry.getKey()); //mapeando o nome do campo para o objeto Field correspondente na classe Product
            if (field != null) { //verificando se o campo existe
                field.setAccessible(true); //tornando o campo acessivel para escrita
                Object valorAtual = ReflectionUtils.getField(field, produto); //pegando o valor atual do campo no produto
                if ((valorAtual == null && entry.getValue() != null) || //verificando se o valor atual é nulo e o novo valor não é nulo
                        (valorAtual != null && !valorAtual.equals(entry.getValue()))) { //ou se o valor atual é diferente do novo valor
                    ReflectionUtils.setField(field, produto, entry.getValue());//atualizando o campo com o novo valor
                    alterado = true; //marcando que uma alteração foi feita
                }
            }
        }

        if (!alterado) { //verificando se realmente houve alguma alteração
            throw new IllegalArgumentException("Nenhuma alteração feita"); //lancando uma exceção se não houve alteração
        }

        return productRepository.save(produto); //salvando o produto atualizado no banco e retornando o produto atualizado
    }

}
