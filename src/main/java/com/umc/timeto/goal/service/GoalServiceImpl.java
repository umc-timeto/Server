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
import com.umc.timeto.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService{

    private final GoalRepository goalRepository;

    // 목표 생성
    @Override
     public ResponseEntity<?> addGoal(GoalAddReqDTO dto, Member member) {
        Goal goal = Goal.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .member(member)
                .build();

        goalRepository.save(goal);

        return ResponseEntity.status(ResponseCode.SUCCESS_ADD_GOAL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ADD_GOAL, dto));
    }

    // 본인의 목표 목록 조회
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<?> getGoalList(Member member) {
        List<GoalResponseDTO> res = goalRepository.findALlByMember(member).stream()
                .map(goal -> GoalResponseDTO.builder()
                        .id(goal.getId())
                        .name(goal.getName())
                        .color(goal.getColor())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_GOALLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_GOALLIST, res));
    }

    // 목표 수정
    @Override
    public ResponseEntity<?> updateGoal(Long goalId, GoalUpdateDTO dto, Member member) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        // 본인 확인: 목표의 주인 ID와 현재 로그인한 유저 ID 비교
        if (!goal.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        // 부분 업데이트
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
    public ResponseEntity<?> deleteGoal(Long goalId, Member member) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        // 본인 확인
        if (!goal.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        goalRepository.delete(goal);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_GOAL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_GOAL, null));
    }
}
