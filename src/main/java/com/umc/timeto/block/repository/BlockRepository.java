package com.umc.timeto.block.repository;

import com.umc.timeto.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByTodo_TodoId(Long todoId);


    List<Block> findByTodo_Folder_Goal_Member_MemberIdAndStartAtBetween(
            Long memberId,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Block> findByTodo_Folder_Goal_Member_MemberIdAndStartAtGreaterThanEqualAndStartAtLessThan(
            Long memberId,
            LocalDateTime start,
            LocalDateTime end
    );


    List<Block> findByTodo_Folder_Goal_Member_MemberIdAndStartAtLessThanAndEndAtGreaterThan(
            Long memberId,
            LocalDateTime endAt,
            LocalDateTime startAt
    );

    List<Block> findByTodo_Folder_Goal_Member_MemberIdAndBlockIdNotAndStartAtLessThanAndEndAtGreaterThan(
            Long memberId,
            Long blockId,
            LocalDateTime endAt,
            LocalDateTime startAt
    );

    Optional<Block> findByBlockIdAndTodo_Folder_Goal_Member_MemberId(Long blockId, Long memberId);
}
