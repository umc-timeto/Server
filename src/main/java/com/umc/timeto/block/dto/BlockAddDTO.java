package com.umc.timeto.block.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "블록 생성 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockAddDTO {

    @Schema(
            description = "블록 시작 시간",
            example = "2026-02-16T14:00",
            type = "string",
            pattern = "yyyy-MM-dd'T'HH:mm"
    )
    @NotNull(message = "startAt은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;
}
