package starter.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EstoqueRequest(@NotNull @Min(value = 0, message = "Estoque nao pode ser negativo") Integer estoque) {}
