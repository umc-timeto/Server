package com.umc.timeto.dailyLog.entity;

import com.umc.timeto.dailyLog.enums.Achievement;
import com.umc.timeto.dailyLog.enums.Satisfaction;
import com.umc.timeto.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Satisfaction answer1; // 1-3 만족도

    @Column(nullable = false)
    private Achievement answer2;  // 1-5 계획 이행

    @Column(nullable = false)
    private String answer3;  // 잘한 점과 아쉬운 점

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(Satisfaction answer1, Achievement answer2, String answer3) {
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }
}
