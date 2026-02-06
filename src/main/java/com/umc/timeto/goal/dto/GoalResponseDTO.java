package com.umc.timeto.goal.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponseDTO {
    private Long id;
    private String name;
    private String color;
}
