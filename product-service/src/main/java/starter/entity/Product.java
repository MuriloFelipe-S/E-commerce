package starter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "product") // nome da tabela no banco de dados
@Entity // anotação que indica que essa classe é uma entidade JPA
@NoArgsConstructor // construtor padrão sem argumentos, necessário para o JPA
@Getter // Lombok para gerar os métodos getters automaticamente
@Setter // Lombok para gerar os métodos setters automaticamente
public class Product {

    @Id // referenciando Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // indicando ao banco que esse campo é autoincrement
    private Long id;

    @NotBlank(message = "campo nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "campo descrição não pode ser vazio")
    private String descricao;

    @NotNull(message = "campo preço não pode ser vazio")
    private BigDecimal preco;

    @NotNull(message = "campo estoque não pode ser vazio")
    private Integer estoque;

    @NotBlank(message = "campo categoria não pode ser vazio")
    private String categoria;

    private String imageUrl;

    @NotNull(message = "campo disponibilidade não pode ser vazio")
    private boolean ativo;

    public Product(String nome, String descricao, BigDecimal preco, Integer estoque, String categoria, String imageUrl, boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.imageUrl = imageUrl;
        this.ativo = ativo;
    }

}
