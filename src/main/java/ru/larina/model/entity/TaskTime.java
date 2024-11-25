package ru.larina.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "task_time")
public class TaskTime extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "stop_time")
    private LocalDateTime stopTime;
    @Column(name = "ds")
    private boolean disabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskTime )) return false;
        return this.getId() != null && this.getId().equals(((TaskTime) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
