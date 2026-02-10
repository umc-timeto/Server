package com.umc.timeto.folder.controller;

import com.umc.timeto.folder.dto.*;
import com.umc.timeto.folder.service.FolderService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderController {

    private final FolderService folderService;

    private Long getMemberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }

    @Operation(summary = "목표별 폴더 리스트", description = "인증된 사용자의 목표별 폴더를 조회합니다")
    @GetMapping("/goal/folder/list")
    public ResponseEntity<?> getFolderList(@RequestParam Long goalId, Authentication authentication) {

        Long memberId = getMemberId(authentication);
        var res = folderService.getFolderList(goalId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_FOLDERLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_FOLDERLIST, res));
    }

    @Operation(summary = "폴더 추가", description = "인증된 사용자가 새로운 폴더를 생성합니다")
    @PostMapping("/folder")
    public ResponseEntity<?> addFolder(
            @RequestParam Long goalId,
            @Valid @RequestBody FolderAddDTO dto,
            Authentication authentication
    ) {
        Long memberId = getMemberId(authentication);
        FolderResponseDTO res = folderService.addFolder(goalId, dto, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ADD_FOLDER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ADD_FOLDER, res));
    }

    @Operation(summary = "폴더 수정", description = "인증된 사용자가 폴더를 수정합니다")
    @PatchMapping("/folder/{folderId}")
    public ResponseEntity<?> updateFolder(
            @PathVariable Long folderId,
            @Valid @RequestBody FolderUpdateDTO dto,
            Authentication authentication
    ) {
        Long memberId = getMemberId(authentication);
        FolderResponseDTO res = folderService.updateFolder(folderId, dto, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_FOLDER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_FOLDER, res));
    }

    @Operation(summary = "폴더 삭제", description = "인증된 사용자가 폴더를 삭제합니다")
    @DeleteMapping("/folder/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long folderId, Authentication authentication) {

        Long memberId = getMemberId(authentication);
        folderService.deleteFolder(folderId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_FOLDER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_FOLDER, null));
    }
}
