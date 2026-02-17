package com.umc.timeto.todo.repository;

import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.domain.enums.TodoState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    //List<Todo> findAllByFolderIdAndStateOrderByTodoIdDesc(Long folderId, TodoState state);
    Optional<Todo> findByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);

    // ✅ folder 안의 progress/complete 리스트
    List<Todo> findAllByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndState(
            Long folderId, Long memberId, TodoState state
    );
    List<Todo> findAllByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndStateOrderBySortOrderAsc(
            Long folderId, Long memberId, TodoState state
    );

    @Query("""
    select coalesce(max(t.sortOrder), 0)
    from Todo t
    where t.folder.folderId = :folderId
      and t.state = :state
      and exists (
           select 1
             from Folder f
             join f.goal g
            where f = t.folder
              and g.member.memberId = :memberId
      )
""")
    int findMaxSortOrder(@Param("folderId") Long folderId,
                         @Param("memberId") Long memberId,
                         @Param("state") TodoState state);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Todo t
       set t.sortOrder = t.sortOrder - 1
     where t.folder.folderId = :folderId
       and t.state = :state
       and t.sortOrder > :deletedOrder
       and exists (
           select 1
             from Folder f
             join f.goal g
            where f = t.folder
              and g.member.memberId = :memberId
       )
""")
    void pullUpAfterDelete(@Param("folderId") Long folderId,
                           @Param("memberId") Long memberId,
                           @Param("state") TodoState state,
                           @Param("deletedOrder") int deletedOrder);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Todo t
       set t.sortOrder = t.sortOrder + 1
     where t.folder.folderId = :folderId
       and t.state = :state
       and t.sortOrder >= :targetOrder
       and t.sortOrder < :currentOrder
       and exists (
           select 1
             from Folder f
             join f.goal g
            where f = t.folder
              and g.member.memberId = :memberId
       )
""")
    void shiftDownForMoveUp(@Param("folderId") Long folderId,
                            @Param("memberId") Long memberId,
                            @Param("state") TodoState state,
                            @Param("targetOrder") int targetOrder,
                            @Param("currentOrder") int currentOrder);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Todo t
       set t.sortOrder = t.sortOrder - 1
     where t.folder.folderId = :folderId
       and t.state = :state
       and t.sortOrder > :currentOrder
       and t.sortOrder <= :targetOrder
       and exists (
           select 1
             from Folder f
             join f.goal g
            where f = t.folder
              and g.member.memberId = :memberId
       )
""")
    void shiftUpForMoveDown(@Param("folderId") Long folderId,
                            @Param("memberId") Long memberId,
                            @Param("state") TodoState state,
                            @Param("currentOrder") int currentOrder,
                            @Param("targetOrder") int targetOrder);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Todo t
       set t.sortOrder = t.sortOrder - 1
     where t.folder.folderId = :folderId
       and t.state = :fromState
       and t.sortOrder > :fromOrder
       and exists (
           select 1
             from Folder f
             join f.goal g
            where f = t.folder
              and g.member.memberId = :memberId
       )
""")
    void pullUpAfterStateMove(@Param("folderId") Long folderId,
                              @Param("memberId") Long memberId,
                              @Param("fromState") TodoState fromState,
                              @Param("fromOrder") int fromOrder);



    // ✅ 내 todo만 삭제
    void deleteByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);

    boolean existsByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);

    // block 후보 조회에 사용
    List<Todo> findByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndBlockIsNull(
            Long folderId,
            Long memberId
    );


    @Query("""
        select t.folder.folderId as folderId, count(t) as cnt
        from Todo t
        where t.folder.goal.id = :goalId
          and t.state = :state
        group by t.folder.folderId
    """)
    List<FolderTodoCountProjection> countTodosGroupByFolder(
            @Param("goalId") Long goalId,
            @Param("state") TodoState state
    );

    // 회원의 할일 전체 삭제(폴더 → 목표 → 회원 기준으로 조인)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from Todo t
        where t.folder.folderId in (
            select f.folderId from Folder f
            where f.goal.id in (
                select g.id from Goal g where g.member.memberId = :memberId
            )
        )
    """)
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
