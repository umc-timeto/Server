package com.umc.timeto.block.controller;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseDTO;
import com.umc.timeto.block.dto.BlockResponseDetailDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;
import com.umc.timeto.block.service.BlockService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
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
public class BlockController implements BlockControllerDocs {

    private final BlockService blockService;

    private Long getMemberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }

    @PatchMapping("/{todoId}")
    @Override
    public ResponseEntity<ResponseDTO<BlockResponseDTO>> createBlock(
            @PathVariable Long todoId,
            @RequestBody BlockAddDTO req,
            Authentication authentication
    ) {
        var res= blockService.createBlock(todoId, req, getMemberId(authentication));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ADD_BLOCK.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ADD_BLOCK, res));
    }


    @GetMapping("/day")
    @Override
    public ResponseEntity<ResponseDTO<List<BlockResponseDetailDTO>>> getBlockByDay(
            // 기본 format: yyyy-MM-DD
            @RequestParam LocalDate date,
            Authentication authentication
    ) {
        var res= blockService.getBlockByDay(date, getMemberId(authentication));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_BLOCKLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_BLOCKLIST, res));
    }


    @GetMapping("/month")
    @Override
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


    @PatchMapping("/{blockId}/duration")
    @Override
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



    @PatchMapping("/{blockId}/move")
    @Override
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
