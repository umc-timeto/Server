package com.umc.timeto.todo.service;

import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TodoCommandService {
    TodoStatusUpdateResponse updateStatus(Long memberId, Long todoId, TodoStatusUpdateRequest request);
    TodoGetResponse updateTodo(Long memberId, Long todoId, TodoUpdateRequest request);
    void deleteTodo(Long memberId, Long todoId);

    @Transactional(readOnly = true)
    List<TodoGetResponse> getUnblockedTodos(Long memberId);
}

