package com.umc.timeto.folder.entity;

import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Builder.Default
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    public Folder(String name, Goal goal) {
        this.name = name;
        this.goal = goal;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
