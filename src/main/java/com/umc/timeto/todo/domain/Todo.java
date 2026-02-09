package com.umc.timeto.todo.domain;


import com.umc.timeto.todo.domain.enums.TodoPriority;
import com.umc.timeto.todo.domain.enums.TodoState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false)
    private Long todoId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // DB가 TIME 컬럼이라면 LocalTime이 가장 안전
    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 30)
    private TodoPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 30)
    private TodoState state;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    // ✅ 지금은 연관관계 없이 숫자 FK만 들고감
    @Column(name = "folder_id", nullable = false)
    private Long folderId;


    public static Todo create(Long folderId, String name, TodoPriority priority, LocalTime duration) {
        Todo todo = new Todo();
        todo.folderId = folderId;
        todo.name = name;
        todo.priority = priority;
        todo.duration = duration;
        todo.state = TodoState.progress; // 기본값
        todo.createAt = LocalDateTime.now();
        return todo;
    }

    public void changeState(TodoState state) {
        this.state = state;
    }

    public void changeName(String name) { this.name = name; }
    public void changePriority(TodoPriority priority) { this.priority = priority; }
    public void changeDuration(LocalTime duration) { this.duration = duration; }

}
