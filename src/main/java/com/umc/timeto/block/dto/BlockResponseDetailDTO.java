package com.umc.timeto.block.dto;

import com.umc.timeto.todo.domain.enums.TodoPriority;
import com.umc.timeto.todo.domain.enums.TodoState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "블록 상세 조회 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDetailDTO {

    @Schema(description = "블록 ID", example = "10")
    private Long blockId;

    @Schema(description = "할 일 ID", example = "5")
    private Long todoId;

    @Schema(description = "블록 시작 시간", example = "2026-02-16T14:00")
    private LocalDateTime startAt;

    @Schema(description = "블록 종료 시간", example = "2026-02-16T16:00")
    private LocalDateTime endAt;

    @Schema(description = "할 일 이름", example = "Spring 정렬 로직 구현")
    private String todoName;

    @Schema(description = "할 일 우선순위", example = "HIGH")
    private TodoPriority priority;

    @Schema(description = "할 일 상태", example = "progress")
    private TodoState state;

    @Schema(description = "목표 이름", example = "백엔드 프로젝트")
    private String goalName;

    @Schema(description = "목표 색상", example = "red")
    private String color;
}