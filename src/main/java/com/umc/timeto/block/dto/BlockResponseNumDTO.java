package com.umc.timeto.block.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "날짜별 블록 개수 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseNumDTO {

    @Schema(description = "날짜", example = "2026-02-16")
    private LocalDate date;

    @Schema(description = "해당 날짜의 블록 개수", example = "3")
    private Long count;
}