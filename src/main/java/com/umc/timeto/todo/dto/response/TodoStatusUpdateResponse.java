package com.umc.timeto.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoStatusUpdateResponse {
    private Long todoId;
    private String state;
}
