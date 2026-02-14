package com.umc.timeto.todo.dto.response;

import com.umc.timeto.todo.domain.enums.TodoPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TodoIngListResponse {
    private int count;
    private List<TodoIngItem> todos;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TodoIngItem {
        private Long todoId;
        private String name;
        private TodoPriority priority;
        private String duration; // "1H 10M" 형태
        private LocalDateTime startAt;
    }
}
