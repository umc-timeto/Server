package com.umc.timeto.folder.entity;

import com.umc.timeto.goal.entity.Goal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    public Folder(String name, Goal goal) {
        this.name = name;
        this.goal = goal;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
