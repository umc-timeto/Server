package com.umc.timeto.folder.repository;

import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByGoal(Goal goal);
}