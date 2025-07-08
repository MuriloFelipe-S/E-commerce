package starter.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionRequest(

        @NotNull
        @DecimalMin(value = "1.0", message = "desconte mínimo de 1%")
        @DecimalMax(value = "75.0", message = "desconto maximo de 75%")
        BigDecimal desconto,

        @NotNull LocalDate dataInicio,

        @NotNull LocalDate dataFim

){}
