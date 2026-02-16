package com.umc.timeto.folder.controller;

import com.umc.timeto.folder.dto.*;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface FolderControllerDocs {

    @Operation(
            summary = "목표별 폴더 리스트 조회",
            description = "인증된 사용자의 특정 목표(goalId)에 속한 폴더 리스트를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "폴더 리스트를 성공적으로 불러왔습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 목표가 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인이 작성한 목표가 아닙니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<ResponseDTO<List<FolderListResponseDTO>>> getFolderList(
            @Parameter(description = "목표 ID", example = "1")
            @RequestParam Long goalId,
            Authentication authentication
    );


    @Operation(summary = "폴더 추가", description = "새로운 폴더를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "폴더를 성공적으로 등록했습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 목표가 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인이 작성한 목표가 아닙니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<ResponseDTO<FolderResponseDTO>> addFolder(
            @Parameter(description = "목표 ID", example = "1")
            @RequestParam Long goalId,
            @Valid @RequestBody FolderAddDTO dto,
            Authentication authentication
    );


    @Operation(summary = "폴더 수정", description = "기존 폴더의 이름을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "폴더를 성공적으로 수정했습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 폴더가 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인이 작성한 목표가 아닙니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<ResponseDTO<FolderResponseDTO>> updateFolder(
            @Parameter(description = "폴더 ID", example = "10")
            @PathVariable Long folderId,
            @Valid @RequestBody FolderUpdateDTO dto,
            Authentication authentication
    );


    @Operation(summary = "폴더 삭제", description = "폴더를 삭제하고 정렬 순서를 재정렬합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "폴더를 성공적으로 삭제했습니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 폴더가 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인이 작성한 목표가 아닙니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<ResponseDTO<Void>> deleteFolder(
            @Parameter(description = "폴더 ID", example = "10")
            @PathVariable Long folderId,
            Authentication authentication
    );


    @Operation(
            summary = "폴더 이동",
            description = """
            폴더를 드래그&드롭으로 이동합니다.
            newIndex는 0 이상 현재 폴더 개수 미만이어야 합니다.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "폴더를 성공적으로 수정했습니다."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "인덱스 범위를 벗어났습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인이 작성한 목표가 아닙니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 폴더가 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<ResponseDTO<Void>> moveFolder(
            @Parameter(description = "폴더 ID", example = "10")
            @PathVariable Long folderId,
            @Parameter(description = "이동할 인덱스", example = "2")
            @RequestParam Integer newIndex,
            Authentication authentication
    );
}
