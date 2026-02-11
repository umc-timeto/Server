package com.umc.timeto.goal.service;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalResponseDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoalService {
    GoalResponseDTO addGoal(GoalAddReqDTO dto, Long memberId);
    List<GoalResponseDTO> getGoalList(Long memberId);
    GoalResponseDTO updateGoal(Long goalId, GoalUpdateDTO dto, Long memberId);
    Long deleteGoal(Long goalId, Long memberId);
}
