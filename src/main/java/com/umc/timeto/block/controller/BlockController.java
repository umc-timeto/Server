package com.umc.timeto.block.controller;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;
import com.umc.timeto.block.service.BlockService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block")
public class BlockController {

    private final BlockService blockService;

    private Long getMemberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }

    @Operation(summary = "타임블럭 저장", description = "할 일 시작 시간을 받아서 블록을 저장합니다. 블록에는 일정 겹침 검사가 존재합니다." +
            "(ex: 일정 1이 7:30~8:30 일떄, 일정2의 생성/이동은 8:30분 이상부터 가능)")
    @PatchMapping("/{todoId}")
    public ResponseEntity<?> createBlock(
            @PathVariable Long todoId,
            @RequestBody BlockAddDTO req,
            Authentication authentication
    ) {
        var res= blockService.createBlock(todoId, req, getMemberId(authentication));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ADD_BLOCK.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ADD_BLOCK, res));
    }

    @Operation(summary = "날짜별 타임블럭 조회", description = "입력받은 날짜(yyyy-MM-DD)에 생성된 타임 블럭들을 조회합니다 블록 조회 기본(메인) 화면에 사용합니다. ")
    @GetMapping("/day")
    public ResponseEntity<?> getBlockByDay(
            // 기본 format: yyyy-MM-DD
            @RequestParam LocalDate date,
            Authentication authentication
    ) {
        var res= blockService.getBlockByDay(date, getMemberId(authentication));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_BLOCKLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_BLOCKLIST, res));
    }

    @Operation(summary = "한달 날짜별 타임블럭 수",
            description = "입력받은 날짜(YYYY-MM)에 생성된 타임 블럭 수를 조회합니다")
    @GetMapping("/month")
    public ResponseEntity<ResponseDTO<List<BlockResponseNumDTO>>> getBlockNumByMonth(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth,
            Authentication authentication
    ) {

        var res = blockService.getBlockNumByMonth(yearMonth, getMemberId(authentication));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_BLOCK_NUMBER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_BLOCK_NUMBER, res));
    }

    @Operation(summary = "블록 소요시간 변경",
            description = "블록 소요시간 변경 시 사용합니다. 할 일 내부에서만 소요시간 변경이 가능하다면 사용하지 않아도 됩니다. ")
    @PatchMapping("/{blockId}/duration")
    public ResponseEntity<ResponseDTO<Void>> updateDuration(
            @PathVariable Long blockId,
            @RequestParam LocalTime duration,
            Authentication authentication
    ) {

        blockService.updateBlockDuration(
                blockId,
                getMemberId(authentication),
                duration
        );

        return ResponseEntity.ok(
                new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_BLOCK, null)
        );
    }


    @Operation(summary = "블록 이동",
            description = "블록을 드래그&드롭으로 이동했을 때 정보를 갱신합니다. 변경된 시작 시간을 입력으로 받습니다. " +
                    "이동된 시간이 다른 일정과 겹칠 경우 갱신되지 않습니다. startAt format: yyyy-MM-dd'T'HH:mm")
    @PatchMapping("/{blockId}/move")
    public ResponseEntity<ResponseDTO<Void>> moveBlock(
            @PathVariable Long blockId,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime startAt,
            Authentication authentication
    ) {

        blockService.moveBlock(
                blockId,
                getMemberId(authentication),
                startAt
        );

        return ResponseEntity.ok(
                new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_BLOCK, null)
        );
    }




}
