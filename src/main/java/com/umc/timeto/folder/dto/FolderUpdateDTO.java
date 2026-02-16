package com.umc.timeto.folder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Schema(description = "폴더 이름 업데이트 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderUpdateDTO {
    @Schema(description = "폴더 이름", example = "백엔드 공부")
    @NotBlank(message = "folderName은 필수 입력 값입니다.")
    private String folderName;
}
