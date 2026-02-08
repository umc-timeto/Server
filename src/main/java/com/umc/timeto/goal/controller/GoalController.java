package com.umc.timeto.goal.controller;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import com.umc.timeto.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    // 목표 등록
    @Operation(
            summary = "목표 등록",
            description = "인증된 유저가 목표를 등록할 수 있습니다."
    )
    @PostMapping
    public ResponseEntity<?> addGoal(
            @RequestBody GoalAddReqDTO dto,
            Authentication authentication
    ) {
        Long memberId = (Long) authentication.getPrincipal();
        return goalService.addGoal(dto, memberId);
    }

    // 목표 리스트 조회
    @Operation(
            summary = "목표 리스트",
            description = "인증된 유저의 목표 리스트를 불러올 수 있습니다."
    )
    @GetMapping("/list")
    public ResponseEntity<?> getGoalList(Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        return goalService.getGoalList(memberId);
    }

    // 목표 수정
    @Operation(
            summary = "목표 수정",
            description = "인증된 유저가 목표를 수정할 수 있습니다."
    )
    @PatchMapping("/{goalId}")
    public ResponseEntity<?> updateGoal(
            @PathVariable Long goalId,
            @RequestBody GoalUpdateDTO dto,
            Authentication authentication
    ) {
        Long memberId = (Long) authentication.getPrincipal();
        return goalService.updateGoal(goalId, dto, memberId);
    }

    // 목표 삭제
    @Operation(
            summary = "목표 삭제",
            description = "인증된 유저가 목표를 삭제할 수 있습니다."
    )
    @DeleteMapping("/{goalId}")
    public ResponseEntity<?> deleteGoal(
            @PathVariable Long goalId,
            Authentication authentication
    ) {
        Long memberId = (Long) authentication.getPrincipal();
        return goalService.deleteGoal(goalId, memberId);
    }
}
