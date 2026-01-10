package com.bancosimplificado.bancosimplificado.domain.Dto;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal value, Long senderId, Long receiverId) {
}
