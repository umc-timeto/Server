package com.umc.timeto.goal.service;

import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalResponseDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService{

    private final GoalRepository goalRepository;

    // 목표 생성
    @Override
     public ResponseEntity<?> addGoal(GoalAddReqDTO dto) {
        Goal goal = Goal.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .build();

        goalRepository.save(goal);

        return ResponseEntity.status(ResponseCode.SUCCESS_ADD_GOAL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ADD_GOAL, dto));
    }

    // 목표 전체 조회
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<?> getGoalList() {
        List<Goal> goals = goalRepository.findAll();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_GOALLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_GOALLIST, goals));
    }

    // 목표 수정
    @Override
    public ResponseEntity<?> updateGoal(Long goalId, GoalUpdateDTO dto) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        // Null 체크 후 부분 업데이트
        if (dto.getName() != null) goal.setName(dto.getName());
        if (dto.getColor() != null) goal.setColor(dto.getColor());

        goalRepository.save(goal); // 변경 사항 저장

        GoalResponseDTO res = GoalResponseDTO.builder()
                .id(goal.getId())
                .name(goal.getName())
                .color(goal.getColor())
                .build();

        return ResponseEntity.status(ResponseCode.SUCCESS_UPDATE_GOAL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_GOAL, res));
    }

    // 4. 목표 삭제 (Delete)
    @Override
    public ResponseEntity<?> deleteGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        goalRepository.delete(goal);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_GOAL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_GOAL, null));
    }
}
