package service;


import entity.Product;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository; //injetando o repositório de produtos

    public ProductService(ProductRepository productRepository) { //construtor da classe que recebe o repositório de produtos
        this.productRepository = productRepository; //inicializa o repositório de produtos
    }

    public Product criarProduto(Product product){ //metodo para criar um produto
        return productRepository.save(product); //salva o produto no banco de dados e retorna o produto salvo
    }

    public Product buscarProdutoPorId(Long id) { //metodo para buscar um produto por ID
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o ID "+ id + "nao foi encontrado")); //retorna o produto se encontrado, caso contrário lança uma exceção
    }

    public List<Product> buscarTodosProdutos() { //metodo para buscar todos os produtos
        return productRepository.findAll(); //retorna todos os produtos do banco de dados
    }

    public Product AtualizarProduto(Long id, Product product) { //metodo para atualizar um produto
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
        return productRepository.findBycategoria(categoria); //retorna os produtos da categoria especificada
    }

    public List<Product> listarProdutosAtivos() { //metodo para listar produtos ativos}
        return productRepository.findAll().stream() //busca todos os produtos e filtra apenas os ativos
                .filter(Product::isAtivo) //filtra os produtos que estão ativos
                .toList(); //converte o resultado para uma lista
    }
}
