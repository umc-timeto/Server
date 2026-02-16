package com.umc.timeto.folder.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Schema(description = "폴더 추가/수정 시 응답 확인용 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponseDTO {
    @Schema(description = "폴더 ID", example = "1")
    private Long id;
    @Schema(description = "폴더 이름", example = "백엔드 공부")
    private String name;

}