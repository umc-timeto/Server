package com.umc.timeto.block.service;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface BlockService {
    BlockResponseDTO createBlock(Long todoId, BlockAddDTO req, Long memberId);


    List<BlockResponseDTO> getBlockByDay(LocalDate date, Long memberId);

    List<BlockResponseNumDTO> getBlockNumByMonth(YearMonth yearMonth, Long memberId);

}
