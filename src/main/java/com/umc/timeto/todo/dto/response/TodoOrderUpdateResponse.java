package com.umc.timeto.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoOrderUpdateResponse {
    private Long todoId;
    private Integer sortOrder;
}
