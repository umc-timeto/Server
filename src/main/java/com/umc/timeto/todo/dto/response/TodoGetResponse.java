package com.umc.timeto.todo.dto.response;

import com.umc.timeto.todo.domain.enums.TodoPriority;
import com.umc.timeto.todo.domain.enums.TodoState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoGetResponse {
    private Long todoId;
    private String name;
    private String duration;     // "1H 30M" 형태로 내려줌
    private TodoPriority priority;
    private TodoState state;
}
