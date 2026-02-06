package com.umc.timeto.goal.controller;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import com.umc.timeto.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    @Operation(
            summary = "목표 등록",
            description = "목표를 등록할 수 있습니다."
    )
    @PostMapping
    public ResponseEntity<?> addGoal(@RequestBody GoalAddReqDTO dto) {
        return goalService.addGoal(dto);
    }

    @Operation(
            summary = "목표 리스트",
            description = "목표 리스트를 불러올 수 있습니다."
    )
    @GetMapping("/list")
    public ResponseEntity<?> getGoalList() {
        return goalService.getGoalList();
    }

    @Operation(
            summary = "목표 수정",
            description = "목표를 수정할 수 있습니다."
    )
    @PatchMapping("/{goalId}")
    public ResponseEntity<?> updateGoal(
            @PathVariable(name = "goalId") Long goalId,
            @RequestBody GoalUpdateDTO dto
    ) {
        return goalService.updateGoal(goalId, dto);
    }

    @Operation(
            summary = "목표 삭제",
            description = "목표를 삭제할 수 있습니다."
    )
    @DeleteMapping("/{goalId}")
    public ResponseEntity<?> deleteGoal(@PathVariable(name = "goalId") Long goalId) {
        return goalService.deleteGoal(goalId);
    }
}
