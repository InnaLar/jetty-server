package ru.larina.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false, length = 200)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(
        mappedBy = "task",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TaskTime> taskTimes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return this.getId() != null && this.getId().equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addTaskTime(TaskTime taskTime) {
        taskTimes.add(taskTime);
        taskTime.setTask(this);
    }

    public void removeTaskTime(TaskTime taskTime) {
        taskTimes.remove(taskTime);
        taskTime.setTask(null);
    }
}
