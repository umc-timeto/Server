package com.umc.timeto.dailyLog.serivce;

import com.umc.timeto.dailyLog.dto.DailyLogMonthlyDTO;
import com.umc.timeto.dailyLog.dto.DailyLogRequestDTO;
import com.umc.timeto.dailyLog.dto.DailyLogResponseDTO;

import java.util.List;

public interface DailyLogService {
    DailyLogResponseDTO saveLog(DailyLogRequestDTO dto, Long memberId);
    List<DailyLogMonthlyDTO> getMonthlyLogs(int year, int month, Long memberId);
    DailyLogResponseDTO getDailyLog(Long logId, Long memberId);
    DailyLogResponseDTO updateLog(Long logId, DailyLogRequestDTO dto, Long memberId);
    Long deleteLog(Long logId, Long memberId)
            ;
}