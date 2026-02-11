package com.umc.timeto.todo.repository;

import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.domain.enums.TodoState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    //List<Todo> findAllByFolderIdAndStateOrderByTodoIdDesc(Long folderId, TodoState state);
    Optional<Todo> findByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);

    // ✅ folder 안의 progress/complete 리스트
    List<Todo> findAllByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndState(
            Long folderId, Long memberId, TodoState state
    );

    // ✅ 내 todo만 삭제
    void deleteByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);

    boolean existsByTodoIdAndFolder_Goal_Member_MemberId(Long todoId, Long memberId);
}
