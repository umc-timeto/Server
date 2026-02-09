package com.umc.timeto.todo.service;

import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;

public interface TodoCommandService {
    TodoStatusUpdateResponse updateStatus(Long todoId, TodoStatusUpdateRequest request);
    TodoGetResponse updateTodo(Long todoId, TodoUpdateRequest request);
    void deleteTodo(Long todoId);
}

