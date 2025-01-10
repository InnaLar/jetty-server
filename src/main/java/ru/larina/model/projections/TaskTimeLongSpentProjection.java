package ru.larina.model.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimeLongSpentProjection {
    private Long id;
    private String startDateTime;
    private String stopDateTime;
    private Duration timeSpent;
}
