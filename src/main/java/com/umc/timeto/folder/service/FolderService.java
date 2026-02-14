package com.umc.timeto.folder.service;

import com.umc.timeto.folder.dto.FolderAddDTO;
import com.umc.timeto.folder.dto.FolderListResponseDTO;
import com.umc.timeto.folder.dto.FolderResponseDTO;
import com.umc.timeto.folder.dto.FolderUpdateDTO;

import java.util.List;

public interface FolderService {

    FolderResponseDTO addFolder(Long goalId, FolderAddDTO dto, Long memberId);

    List<FolderListResponseDTO> getFolderList(Long goalId, Long memberId);

    FolderResponseDTO updateFolder(Long folderId, FolderUpdateDTO dto, Long memberId);

    void deleteFolder(Long folderId, Long memberId);
}