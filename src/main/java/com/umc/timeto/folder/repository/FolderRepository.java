package com.umc.timeto.folder.repository;

import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    Optional<Folder> findByFolderIdAndGoal_Member_MemberId(Long folderId, Long memberId);
    List<Folder> findAllByGoalOrderBySortOrderAsc(Goal goal);
    Optional<Folder> findTopByGoalOrderBySortOrderDesc(Goal goal);

    // 회원의 폴더 전체 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from Folder f
        where f.goal.id in (
            select g.id from Goal g where g.member.memberId = :memberId
        )
    """)
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}