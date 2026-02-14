package com.umc.timeto.block.service;

import com.umc.timeto.block.dto.BlockAddDTO;
import com.umc.timeto.block.dto.BlockResponseDTO;
import com.umc.timeto.block.dto.BlockResponseDetailDTO;
import com.umc.timeto.block.dto.BlockResponseNumDTO;
import com.umc.timeto.block.entity.Block;
import com.umc.timeto.block.repository.BlockRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockServiceImpl implements BlockService {

    private final TodoRepository todoRepository;
    private final BlockRepository blockRepository;

    @Override
    public BlockResponseDTO createBlock(Long todoId, BlockAddDTO req, Long memberId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));


        // 블록 겹침 조회
        LocalDateTime startAt = req.getStartAt();
        LocalTime duration = todo.getDuration();

        LocalDateTime endAt = startAt
                .plusHours(duration.getHour())
                .plusMinutes(duration.getMinute())
                .plusSeconds(duration.getSecond());
        System.out.println("시작시간" +startAt + "끝나는시간" +endAt);

        List<Block> overlaps =
                blockRepository
                        .findByTodo_Folder_Goal_Member_MemberIdAndStartAtLessThanAndEndAtGreaterThan(
                                memberId,
                                endAt,
                                startAt
                        );

        if (!overlaps.isEmpty()) {
            throw new GlobalException(ErrorCode.BLOCK_TIME_CONFLICT);
        }


        //블록 저장
        Block block = new Block(todo, startAt);
        Block savedBlock = blockRepository.save(block);


        return BlockResponseDTO.builder()
                .blockId(savedBlock.getBlockId())
                .todoId(todoId)
                .startAt(savedBlock.getStartAt())
                .endAt(savedBlock.getEndAt())
                .build();
    }

    @Override
    public List<BlockResponseDetailDTO> getBlockByDay(LocalDate date, Long memberId) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Block> blocks =
                blockRepository
                        .findByTodo_Folder_Goal_Member_MemberIdAndStartAtBetween(
                                memberId,
                                start,
                                end
                        );


        return blocks.stream()
                .map(block -> BlockResponseDetailDTO.builder()
                        .blockId(block.getBlockId())
                        .todoId(block.getTodo().getTodoId())
                        .startAt(block.getStartAt())
                        .endAt(block.getEndAt())
                        .todoName(block.getTodo().getName())
                        .priority(block.getTodo().getPriority())
                        .state(block.getTodo().getState())
                        .goalName(block.getTodo().getFolder().getGoal().getName())
                        .color(block.getTodo().getFolder().getGoal().getColor())
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
                blockRepository.findByTodo_Folder_Goal_Member_MemberIdAndStartAtGreaterThanEqualAndStartAtLessThan(
                        memberId,
                        start,
                        nextMonthStart
                );

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

    @Override
    public BlockResponseDTO updateBlockDuration(Long blockId, Long memberId, LocalTime newDuration) {

        Block block = blockRepository
                .findByBlockIdAndTodo_Folder_Goal_Member_MemberId(blockId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.BLOCK_NOT_FOUND));

        LocalDateTime newStart = block.getStartAt();
        LocalDateTime newEnd = newStart
                .plusHours(newDuration.getHour())
                .plusMinutes(newDuration.getMinute())
                .plusSeconds(newDuration.getSecond());

        List<Block> overlaps =
                blockRepository
                        .findByTodo_Folder_Goal_Member_MemberIdAndBlockIdNotAndStartAtLessThanAndEndAtGreaterThan(
                                memberId,
                                blockId,
                                newEnd,
                                newStart
                        );

        if (!overlaps.isEmpty()) {
            throw new GlobalException(ErrorCode.BLOCK_TIME_CONFLICT);
        }

        block.updateTime(newStart, newEnd);

        // Todo 동기화
        Todo todo = block.getTodo();
        todo.changeDuration(newDuration);
        todo.updateStartAt(newStart);


        return BlockResponseDTO.builder()
                .blockId(block.getBlockId())
                .todoId(todo.getTodoId())
                .startAt(block.getStartAt())
                .endAt(block.getEndAt())
                .build();
    }


    @Override
    public void updateBlockDurationByTodo(Long todoId,
                                          Long memberId,
                                          LocalTime newDuration) {

        Todo todo = todoRepository
                .findByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        Optional<Block> optionalBlock =
                blockRepository.findByTodo_TodoId(todoId);

        // Block이 존재하는 경우
        if (optionalBlock.isPresent()) {

            Block block = optionalBlock.get();

            LocalDateTime newStart = block.getStartAt();
            LocalDateTime newEnd = newStart
                    .plusHours(newDuration.getHour())
                    .plusMinutes(newDuration.getMinute())
                    .plusSeconds(newDuration.getSecond());

            // 충돌 검사
            List<Block> overlaps =
                    blockRepository
                            .findByTodo_Folder_Goal_Member_MemberIdAndBlockIdNotAndStartAtLessThanAndEndAtGreaterThan(
                                    memberId,
                                    block.getBlockId(),
                                    newEnd,
                                    newStart
                            );

            if (!overlaps.isEmpty()) {
                throw new GlobalException(ErrorCode.BLOCK_TIME_CONFLICT);
            }

            block.updateTime(newStart, newEnd);
            todo.changeDuration(newDuration);
            todo.updateStartAt(newStart);

        }
        // Block이 없는 경우
        else {
            todo.changeDuration(newDuration);
        }
    }




    @Override
    public BlockResponseDTO moveBlock(Long blockId, Long memberId, LocalDateTime newStart) {

        Block block = blockRepository
                .findByBlockIdAndTodo_Folder_Goal_Member_MemberId(blockId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.BLOCK_NOT_FOUND));

        LocalTime duration = block.getTodo().getDuration();

        LocalDateTime newEnd = newStart
                .plusHours(duration.getHour())
                .plusMinutes(duration.getMinute())
                .plusSeconds(duration.getSecond());

        List<Block> overlaps =
                blockRepository
                        .findByTodo_Folder_Goal_Member_MemberIdAndBlockIdNotAndStartAtLessThanAndEndAtGreaterThan(
                                memberId,
                                blockId,
                                newEnd,
                                newStart
                        );

        if (!overlaps.isEmpty()) {
            throw new GlobalException(ErrorCode.BLOCK_TIME_CONFLICT);
        }

        block.updateTime(newStart, newEnd);

        //  Todo 동기화
        Todo todo = block.getTodo();
        todo.updateStartAt(newStart);
        long seconds = java.time.Duration.between(newStart, newEnd).getSeconds();
        LocalTime newDuration = LocalTime.ofSecondOfDay(seconds);
        todo.changeDuration(newDuration);

        return BlockResponseDTO.builder()
                .blockId(block.getBlockId())
                .todoId(todo.getTodoId())
                .startAt(block.getStartAt())
                .endAt(block.getEndAt())
                .build();

    }

}
