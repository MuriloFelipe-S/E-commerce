package starter.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionRequest(


        @NotNull  @DecimalMin("0.0") BigDecimal desconto,

        @NotNull LocalDate dataInicio,

        @NotNull LocalDate dataFim
) {
}
