package ru.larina.model.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimeLongSpentProjection {
    private Long id;
    private String startDateTime;
    private String stopDateTime;
    private Duration timeSpent;
}
