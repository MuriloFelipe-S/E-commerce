package starter.DTO;

import starter.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromotionResponse(
        BigDecimal precoOriginal,
        BigDecimal precoComDesconto,
        BigDecimal descontoPercentual,
        LocalDate dataInicio,
        LocalDate dataFim

) { }
