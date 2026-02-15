package com.umc.timeto.dailyLog.controller;

import com.umc.timeto.dailyLog.dto.DailyLogRequestDTO;
import com.umc.timeto.dailyLog.serivce.DailyLogService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/logs")
public class DailyLogController {
    private final DailyLogService dailyLogService;

    // 일지 등록
    @Operation(
            summary = "일지 등록",
            description = "인증된 유저가 일지를 등록할 수 있습니다."
    )
    @PostMapping
    public ResponseEntity<?> saveLog(@RequestBody @Valid DailyLogRequestDTO dto,
                                     Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        var result = dailyLogService.saveLog(dto, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_SAVE_LOG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_LOG, result));
    }

    // 월별 일지 조회
    @Operation(summary = "월별 일지 조회", description = "월별 일지 리스트를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyLogs(@RequestParam int year,
                                            @RequestParam int month,
                                            Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        var result = dailyLogService.getMonthlyLogs(year, month, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_MONTHLY_LOGS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_MONTHLY_LOGS, result));
    }

    @Operation(summary = "일별 일지 조회", description = "일지를 조회합니다.")
    @GetMapping("/{logId}")
    public ResponseEntity<?> getDailyLogs(@PathVariable Long logId,
                                          Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();

        var result = dailyLogService.getDailyLog(logId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_DAILY_LOGS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_DAILY_LOGS, result));
    }

    // 일지 수정
    @Operation(
            summary = "일지 수정",
            description = "작성한 일지의 내용을 수정합니다.")
    @PatchMapping("/{logId}")
    public ResponseEntity<?> updateLog(@PathVariable Long logId,
                                       @RequestBody @Valid DailyLogRequestDTO dto,
                                       Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        var result = dailyLogService.updateLog(logId, dto, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_LOG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_LOG, result));
    }

    // 일지 삭제
    @Operation(summary = "일지 삭제", description = "작성한 일지를 삭제합니다.")
    @DeleteMapping("/{logId}")
    public ResponseEntity<?> deleteLog(@PathVariable Long logId,
                                       Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        dailyLogService.deleteLog(logId, memberId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_LOG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_LOG, null));
    }
}