package com.umc.timeto.todo.repository;

import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.domain.enums.TodoState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByFolderIdAndStateOrderByTodoIdDesc(Long folderId, TodoState state);

}
