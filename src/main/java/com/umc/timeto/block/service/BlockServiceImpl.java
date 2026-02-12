package com.umc.timeto.block.service;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;
import com.umc.timeto.block.entity.Block;
import com.umc.timeto.block.repository.BlockRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final TodoRepository todoRepository;
    private final BlockRepository blockRepository;

    @Override
    public BlockResponseDTO createBlock(Long todoId, BlockAddDTO req, Long memberId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        Block block = new Block(todo, req.getStartAt());
        Block savedBlock = blockRepository.save(block);

        return BlockResponseDTO.builder()
                .blockId(savedBlock.getBlockId())
                .todoId(todoId)
                .startAt(savedBlock.getStartAt())
                .endAt(savedBlock.getEndAt())
                .build();
    }

    @Override
    public List<BlockResponseDTO> getBlockByDay(LocalDate date, Long memberId) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Block> blocks = blockRepository.findByStartAtBetween(start, end);

        return blocks.stream()
                .map(block -> BlockResponseDTO.builder()
                        .blockId(block.getBlockId())
                        .todoId(block.getTodo().getTodoId())
                        .startAt(block.getStartAt())
                        .endAt(block.getEndAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockResponseNumDTO> getBlockNumByMonth(YearMonth yearMonth, Long memberId) {

        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime nextMonthStart = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        List<Block> blocks =
                blockRepository.findByStartAtGreaterThanEqualAndStartAtLessThan(start, nextMonthStart);

        return blocks.stream()
                .collect(Collectors.groupingBy(
                        block -> block.getStartAt().toLocalDate(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> new BlockResponseNumDTO(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
    }

}
