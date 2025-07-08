package starter.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import starter.enumCategorias.ProductCategory;
import starter.enumCategorias.SubCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @NotNull(message = "campo categoria não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private ProductCategory categoria;

    @NotNull(message = "campo subcategoria não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private SubCategory subCategoria;

    private String imageUrl;

    @NotNull(message = "campo disponibilidade não pode ser vazio")
    private boolean ativo;

    @JsonInclude(JsonInclude.Include.NON_NULL) private BigDecimal desconto;

    @JsonInclude(JsonInclude.Include.NON_NULL) private LocalDate dataInicio;

    @JsonInclude(JsonInclude.Include.NON_NULL) private LocalDate dataFim;

    public Product(String nome, String descricao, BigDecimal preco, Integer estoque, ProductCategory categoria, SubCategory subCategoria, String imageUrl, boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.imageUrl = imageUrl;
        this.ativo = ativo;
    }

}
