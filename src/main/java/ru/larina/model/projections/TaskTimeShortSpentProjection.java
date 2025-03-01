package ru.larina.model.projections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskTimeShortSpentProjection {
    private Long id;
    private BigDecimal sumTime;
}
