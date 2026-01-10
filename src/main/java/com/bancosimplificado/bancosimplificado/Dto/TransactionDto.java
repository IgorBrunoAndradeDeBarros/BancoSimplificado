package com.bancosimplificado.bancosimplificado.Dto;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal value, Long senderId, Long receiverId) {
}
