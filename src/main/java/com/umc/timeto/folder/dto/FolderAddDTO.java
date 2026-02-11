package com.umc.timeto.folder.dto;


import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.goal.entity.Goal;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderAddDTO {

    @NotBlank(message = "folderName은 필수 입력 값입니다.")
    private String folderName;

    public Folder toEntity(Goal goal) {
        return Folder.builder()
                .name(folderName)
                .goal(goal)
                .build();
    }
}