package com.umc.timeto.folder.service;

import com.umc.timeto.folder.dto.FolderAddDTO;
import com.umc.timeto.folder.dto.FolderListResponseDTO;
import com.umc.timeto.folder.dto.FolderResponseDTO;
import com.umc.timeto.folder.dto.FolderUpdateDTO;
import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.folder.repository.FolderRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.goal.repository.GoalRepository;
import com.umc.timeto.todo.domain.enums.TodoState;
import com.umc.timeto.todo.repository.FolderTodoCountProjection;
import com.umc.timeto.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional

@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final GoalRepository goalRepository;
    private final TodoRepository todoRepository;

    @Override
    public FolderResponseDTO addFolder(Long goalId, FolderAddDTO dto, Long memberId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        if (!goal.getMember().getMemberId().equals(memberId)){
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        Folder folder = folderRepository.save(dto.toEntity(goal));

        return FolderResponseDTO.builder()
                .id(folder.getFolderId())
                .name(folder.getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FolderListResponseDTO> getFolderList(Long goalId, Long memberId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        if (!goal.getMember().getMemberId().equals(memberId)) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }
        List<FolderTodoCountProjection> counts =
                todoRepository.countTodosGroupByFolder(goalId, TodoState.progress);

        Map<Long, Long> countMap = counts.stream()
                .collect(Collectors.toMap(
                        FolderTodoCountProjection::getFolderId,
                        FolderTodoCountProjection::getCnt
                ));

        return folderRepository.findAllByGoal(goal)
                .stream()
                .map(folder -> FolderListResponseDTO.builder()
                        .id(folder.getFolderId())
                        .name(folder.getName())
                        .ingTodoCount(countMap.getOrDefault(folder.getFolderId(), 0L))
                        .build())
                .toList();
    }

    @Override
    public FolderResponseDTO updateFolder(Long folderId, FolderUpdateDTO dto, Long memberId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.FOLDER_NOT_FOUND));

        if (!folder.getGoal().getMember().getMemberId().equals(memberId)) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        folder.updateName(dto.getFolderName());

        return FolderResponseDTO.builder()
                .id(folder.getFolderId())
                .name(folder.getName())
                .build();
    }

    @Override
    public void deleteFolder(Long folderId, Long memberId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.FOLDER_NOT_FOUND));

        if (!folder.getGoal().getMember().getMemberId().equals(memberId)) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        folderRepository.delete(folder);
    }
}
