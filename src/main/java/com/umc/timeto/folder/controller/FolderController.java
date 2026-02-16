package com.umc.timeto.folder.controller;

import com.umc.timeto.folder.dto.*;
import com.umc.timeto.folder.service.FolderService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderController implements FolderControllerDocs {

    private final FolderService folderService;

    private Long getMemberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }


    @GetMapping("/goal/folder/list")
    @Override
    public ResponseEntity<ResponseDTO<List<FolderListResponseDTO>>> getFolderList(@RequestParam Long goalId, Authentication authentication) {

        Long memberId = getMemberId(authentication);
        var res = folderService.getFolderList(goalId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_FOLDERLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_FOLDERLIST, res));
    }

    @PostMapping("/folder")
    @Override
    public ResponseEntity<ResponseDTO<FolderResponseDTO>> addFolder(
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


    @PatchMapping("/folder/{folderId}")
    @Override
    public ResponseEntity<ResponseDTO<FolderResponseDTO>> updateFolder(
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

    @DeleteMapping("/folder/{folderId}")
    @Override
    public ResponseEntity<ResponseDTO<Void>> deleteFolder(@PathVariable Long folderId, Authentication authentication) {

        Long memberId = getMemberId(authentication);
        folderService.deleteFolder(folderId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_FOLDER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_FOLDER, null));
    }

    @PatchMapping("/folder/{folderId}/move")
    @Override
    public ResponseEntity<ResponseDTO<Void>> moveFolder(
            @PathVariable Long folderId,
            @RequestParam Integer newIndex,
            Authentication authentication
    ) {

        Long memberId = getMemberId(authentication);

        folderService.moveFolder(folderId, memberId, newIndex);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_FOLDER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_FOLDER, null));
    }


}
