package com.umc.timeto.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalUpdateDTO {
    private String name;
    private String color;
}
