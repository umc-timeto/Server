package com.umc.timeto.block.entity;

import com.umc.timeto.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endAt;
    // block에서는 startAt, endAt 입력받고 todoo 에 startAt 저장 및 duration update
    //Duration: localtime

    public Block(Todo todo, LocalDateTime startAt) {
        this.todo=todo;

        LocalDateTime normalizedStart = normalize(startAt);
        this.startAt = normalizedStart;
        LocalTime duration = todo.getDuration();

        LocalDateTime calculatedEnd = normalizedStart
                .plusHours(duration.getHour())
                .plusMinutes(duration.getMinute());

        this.endAt = normalize(calculatedEnd);
    }

    public void updateTime(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = normalize(startAt);
        this.endAt = normalize(endAt);
    }

    private LocalDateTime normalize(LocalDateTime time) {
        return time.withSecond(0).withNano(0);
    }

    @PrePersist
    @PreUpdate
    private void trimSeconds() {
        if (startAt != null) {
            startAt = startAt.withSecond(0).withNano(0);
        }
        if (endAt != null) {
            endAt = endAt.withSecond(0).withNano(0);
        }
    }



}