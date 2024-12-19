package ru.larina.model.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalWorkByPeriodProjection {
    private BigDecimal totalTime;
}
