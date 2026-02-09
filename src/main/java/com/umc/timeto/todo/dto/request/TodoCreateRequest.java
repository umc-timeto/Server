package com.umc.timeto.todo.dto.request;

import com.umc.timeto.todo.domain.enums.TodoPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TodoCreateRequest {
    @NotBlank
    private String name;

    @NotNull
    private TodoPriority priority;

    /**
     * 예: "1H 30M"
     * - 프론트가 보내주는 포맷 그대로 받음
     */
    @NotBlank
    private String duration;
}
