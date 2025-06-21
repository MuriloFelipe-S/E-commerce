package entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "product")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id // referenciando Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // indicando ao banco que esse campo é autoincrement

    @NotBlank(message = "campo id não pode ser vazio")
    private Long id;

    @NotBlank(message = "campo nome não pode ser vazio")
    private String name;

    @NotBlank(message = "campo descrição não pode ser vazio")
    private String description;

    @NotBlank(message = "campo preço não pode ser vazio")
    private Double price;

    public Product(Long id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
