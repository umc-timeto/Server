package com.umc.timeto.block.controller;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseDTO;
import com.umc.timeto.block.dto.BlockResponseDetailDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface BlockControllerDocs {
    @Operation(summary = "타임블럭 저장", description = "할 일 시작 시간을 받아서 블록을 저장합니다. 블록에는 일정 겹침 검사가 존재합니다." +
            "(ex: 일정 1이 7:30~8:30 일떄, 일정2의 생성/이동은 8:30분 이상부터 가능)" +
            "추가: startAt이 해당하는 날짜(당일 05시~다음날 05시 사이) 에 블록이 하나도 없을 경우에는 startAt의 값을 입력받아 생성됩니다." +
            "하나라도 블록이 존재하면 가장 하단 블록의 endAt 시간대를 startAt으로 하여 생성됩니다. " +
            "startAt의 날짜 부분에는 선택한 날짜를, 시간에는 현재 시간대나 default로 설정하고 싶은 시간을 입력해주세요.  " )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "블록을 성공적으로 등록했습니다.",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 해당 시간에 블록이 존재합니다.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 할 일이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PatchMapping("/{todoId}")
    ResponseEntity<ResponseDTO<BlockResponseDTO>> createBlock(
            @PathVariable Long todoId,
            @RequestBody BlockAddDTO req,
            Authentication authentication
    );



    @Operation(summary = "날짜별 타임블럭 조회", description = "입력받은 날짜(yyyy-MM-DD)에 생성된 타임 블럭들을 조회합니다 블록 조회 기본(메인) 화면에 사용합니다. ")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "블록 리스트를 성공적으로 불러왔습니다."
            )
    })
    @GetMapping("/day")
    ResponseEntity<ResponseDTO<List<BlockResponseDetailDTO>>> getBlockByDay(
            // 기본 format: yyyy-MM-DD
            @RequestParam LocalDate date,
            Authentication authentication
    );

    @Operation(summary = "한달 날짜별 타임블럭 수",
            description = "입력받은 날짜(YYYY-MM)에 생성된 타임 블럭 수를 조회합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "블록 수를 성공적으로 불러왔습니다."
            )
    })
    @GetMapping("/month")
    ResponseEntity<ResponseDTO<List<BlockResponseNumDTO>>> getBlockNumByMonth(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth,
            Authentication authentication
    );

//    @Operation(summary = "블록 소요시간 변경",
//            description = "블록 소요시간 변경 시 사용합니다. 할 일 내부에서만 소요시간 변경이 가능하다면 사용하지 않아도 됩니다. ")
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "블록을 성공적으로 업데이트했습니다"
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "해당 아이디를 가진 블록이 존재하지 않습니다.",
//                    content = @Content(schema = @Schema(hidden = true))
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "이미 해당 시간에 블록이 존재합니다."
//                    ,content = @Content(schema = @Schema(hidden = true))
//            )
//
//    })
//    @PatchMapping("/{blockId}/duration")
//    ResponseEntity<ResponseDTO<Void>> updateDuration(
//            @PathVariable Long blockId,
//            @RequestParam LocalTime duration,
//            Authentication authentication
//    );

    @Operation(summary = "블록 이동",
            description = "블록을 드래그&드롭으로 이동했을 때 정보를 갱신합니다. 변경된 시작 시간을 입력으로 받습니다. " +
                    "이동된 시간이 다른 일정과 겹칠 경우 갱신되지 않습니다. startAt format: yyyy-MM-dd'T'HH:mm(ex: 2026-02-14T:02:14) ")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "블록을 성공적으로 업데이트했습니다"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 해당 시간에 블록이 존재합니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 아이디를 가진 블록이 존재하지 않습니다."
                    ,content = @Content(schema = @Schema(hidden = true))
            )

    })
    @PatchMapping("/{blockId}/move")
    ResponseEntity<ResponseDTO<Void>> moveBlock(
            @PathVariable Long blockId,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime startAt,
            Authentication authentication
    );
}
