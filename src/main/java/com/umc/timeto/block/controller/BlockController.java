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

    @Operation(summary = "타임블럭 저장", description = "할 일 시작 시간을 받아서 블록을 저장합니다")
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

    @Operation(summary = "날짜별 타임블럭 조회", description = "입력받은 날짜(YYYY-MM-DD)에 생성된 타임 블럭들을 조회합니다")
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

}
