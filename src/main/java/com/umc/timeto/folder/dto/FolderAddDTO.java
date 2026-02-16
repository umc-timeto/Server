package com.umc.timeto.folder.dto;


import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.goal.entity.Goal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "폴더 생성 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderAddDTO {
    @Schema(description = "폴더 이름", example = "백엔드 공부")
    @NotBlank(message = "folderName은 필수 입력 값입니다.")
    private String folderName;

    public Folder toEntity(Goal goal) {
        return Folder.builder()
                .name(folderName)
                .goal(goal)
                .build();
    }
}