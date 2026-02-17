package com.umc.timeto.todo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TodoOrderUpdateRequest {
    @NotNull
    @Min(1)
    private Integer targetOrder;
}
