package com.umc.timeto.todo.dto.request;

import com.umc.timeto.todo.domain.enums.TodoPriority;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoUpdateRequest {
    private String name;
    private TodoPriority priority;

    /**
     * "1H", "1H 30M", "90M" 등
     * 안 오면 변경 안 함
     */
    private String duration;
}
