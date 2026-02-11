package com.umc.timeto.folder.service;

import com.umc.timeto.folder.dto.FolderAddDTO;
import com.umc.timeto.folder.dto.FolderResponseDTO;
import com.umc.timeto.folder.dto.FolderUpdateDTO;
import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.folder.repository.FolderRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final GoalRepository goalRepository;

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
    public List<FolderResponseDTO> getFolderList(Long goalId, Long memberId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GlobalException(ErrorCode.GOAL_NOT_FOUND));

        if (!goal.getMember().getMemberId().equals(memberId)) {
            throw new GlobalException(ErrorCode.GOAL_FORBIDDEN);
        }

        return folderRepository.findAllByGoal(goal)
                .stream()
                .map(folder -> FolderResponseDTO.builder()
                        .id(folder.getFolderId())
                        .name(folder.getName())
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
