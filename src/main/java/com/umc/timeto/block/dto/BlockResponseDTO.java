package com.umc.timeto.block.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDTO {
    private Long blockId;
    private Long todoId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
