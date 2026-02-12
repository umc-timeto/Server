package com.umc.timeto.block.repository;

import com.umc.timeto.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByStartAtBetween(LocalDateTime start, LocalDateTime end);
    List<Block> findByStartAtGreaterThanEqualAndStartAtLessThan(LocalDateTime start, LocalDateTime nextMonthStart);
}
