package starter.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionRequest(

        @NotNull
        @DecimalMin(value = "0.0", message = "desconte mínimo de 0%")
        @DecimalMax(value = "100.0", message = "desconto maximo de 100%")
        BigDecimal desconto,

        @NotNull LocalDate dataInicio,

        @NotNull LocalDate dataFim

){}
