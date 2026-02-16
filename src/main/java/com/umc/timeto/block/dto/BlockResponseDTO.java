package com.umc.timeto.block.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "블록 생성/수정 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDTO {

    @Schema(description = "블록 ID", example = "10")
    private Long blockId;

    @Schema(description = "연결된 할 일 ID", example = "5")
    private Long todoId;

    @Schema(
            description = "블록 시작 시간",
            example = "2026-02-16T14:00"
    )
    private LocalDateTime startAt;

    @Schema(
            description = "블록 종료 시간",
            example = "2026-02-16T16:00"
    )
    private LocalDateTime endAt;
}
