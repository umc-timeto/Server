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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockServiceImpl implements BlockService {

    private final TodoRepository todoRepository;
    private final BlockRepository blockRepository;
    private final BusinessDayPolicy businessDayPolicy;

    @Override
    public BlockResponseDTO createBlock(Long todoId, BlockAddDTO req, Long memberId) {

        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId,memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        LocalDateTime startAt = req.getStartAt();

        // Todo당 Block 1개 보장
        blockRepository.findByTodo_TodoId(todoId)
                .ifPresent(b -> {
                    throw new GlobalException(ErrorCode.BAD_REQUEST);
                });


        // 05:00 기준 startAt에서 입력받은 날짜 범위
        LocalDateTime dayStart = businessDayPolicy.startOfBusinessDay(startAt);
        LocalDateTime dayEnd = businessDayPolicy.endOfBusinessDay(startAt);



        List<Block> todayBlocks =
                blockRepository
                        .findByTodo_Folder_Goal_Member_MemberIdAndStartAtGreaterThanEqualAndStartAtLessThan(
                                memberId,
                                dayStart,
                                dayEnd
                        );


        System.out.println("dayStart = " + dayStart);
        System.out.println("dayEnd = " + dayEnd);
        System.out.println("todayBlocks size = " + todayBlocks.size());




        if (todayBlocks.isEmpty()) {
            // 오늘 블록 없으면 입력받은 시간에 생성
            startAt = req.getStartAt();
        } else {
            // 가장 늦게 끝나는 블록 아래 배치
            Block lastBlock = todayBlocks.stream()
                    .max(Comparator.comparing(Block::getEndAt))
                    .orElseThrow();

            startAt = lastBlock.getEndAt();
        }

        //생성 시 시작시간 추가되도록
        todo.updateStartAt(startAt);

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

        LocalDateTime time_standard = date.atTime(5, 0);

        LocalDateTime start = businessDayPolicy.startOfBusinessDay(time_standard);
        LocalDateTime end = businessDayPolicy.endOfBusinessDay(time_standard);

        List<Block> blocks =
                blockRepository.findBlocksWithTodo(
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
