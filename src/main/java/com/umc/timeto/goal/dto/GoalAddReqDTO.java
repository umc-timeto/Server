package com.umc.timeto.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalAddReqDTO {
    private String name;
    private String color;
}
