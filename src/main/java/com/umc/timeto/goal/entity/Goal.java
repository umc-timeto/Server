package com.umc.timeto.goal.entity;

import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "goal_id")
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
