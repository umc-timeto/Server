package com.umc.timeto.folder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderUpdateDTO {

    @NotBlank(message = "folderName은 필수 입력 값입니다.")
    private String folderName;
}
