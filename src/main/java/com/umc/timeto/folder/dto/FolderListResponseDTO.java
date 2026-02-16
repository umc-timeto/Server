package com.umc.timeto.folder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "폴더 목록 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderListResponseDTO {
    @Schema(description = "폴더 ID", example = "1")
    private Long id;
    @Schema(description = "폴더 이름", example = "백엔드 공부")
    private String name;
    @Schema(description = "진행 중인 할 일 개수", example = "3")
    private long ingTodoCount;
}
