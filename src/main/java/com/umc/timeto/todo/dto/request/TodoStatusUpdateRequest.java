package com.umc.timeto.todo.dto.request;

import com.umc.timeto.todo.domain.enums.TodoState;
import jakarta.validation.constraints.NotNull;

public class TodoStatusUpdateRequest {
    @NotNull
    private TodoState state;

    public TodoState getState() {
        return state;
    }
}
