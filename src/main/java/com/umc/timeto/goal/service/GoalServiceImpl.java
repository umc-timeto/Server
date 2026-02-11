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
import com.umc.timeto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    // 목표 생성
    @Override
    public GoalResponseDTO addGoal(GoalAddReqDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Goal goal = Goal.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .member(member)
                .build();

        Goal savedGoal = goalRepository.save(goal);

        return GoalResponseDTO.builder()
                .id(savedGoal.getId())
                .name(savedGoal.getName())
                .color(savedGoal.getColor())
                .build();
    }

    // 본인의 목표 목록 조회
    @Transactional(readOnly = true)
    @Override
    public List<GoalResponseDTO> getGoalList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        return goalRepository.findALlByMember(member).stream()
                .map(goal -> GoalResponseDTO.builder()
                        .id(goal.getId())
                        .name(goal.getName())
                        .color(goal.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    // 목표 수정
    @Override
    public GoalResponseDTO updateGoal(Long goalId, GoalUpdateDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        // 본인 확인
        if (!goal.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        if (dto.getName() != null) goal.setName(dto.getName());
        if (dto.getColor() != null) goal.setColor(dto.getColor());

        return GoalResponseDTO.builder()
                .id(goal.getId())
                .name(goal.getName())
                .color(goal.getColor())
                .build();
    }

    // 목표 삭제
    @Override
    public Long deleteGoal(Long goalId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        // 본인 확인
        if (!goal.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        goalRepository.delete(goal);
        return goalId;
    }
}
