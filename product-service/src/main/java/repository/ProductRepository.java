package repository;

import entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // indica que essa interface é um repositório Spring Data JPA
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Aqui podemos adicionar métodos personalizados de consulta, se necessário.
    // Por exemplo, podemos buscar produtos por nome ou preço, etc.

    // Exemplo:
    // List<Product> findByNameContaining(String name);

     //metodo para buscar produtos por categoria

    List<Product> findByCategoria(String categoria);
}
