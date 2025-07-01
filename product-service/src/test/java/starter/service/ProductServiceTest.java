package starter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import starter.entity.Product;
import starter.repository.ProductRepository;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void RetornarProdutoSeIdExistir() {
        Long idExistente = 1L; // ID de exemplo que deve existir no repositório
        Product product = new Product();
        product.setId(idExistente);
        product.setNome("Produto de Teste");
        product.setDescricao("Descricao do Produto de Teste");

        when(productRepository.findById(idExistente)).thenReturn(Optional.of(product));

        Product produtoRetornado = productService.buscarProdutoPorId(idExistente);

        assertNotNull(produtoRetornado);
        assertEquals(idExistente, produtoRetornado.getId());
    }


}
