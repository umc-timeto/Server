package com.umc.timeto.todo.service;

import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoIngListResponse;

public interface TodoQueryService {
    TodoGetResponse getTodo(Long todoId);
    TodoIngListResponse getInProgressTodos(Long folderId);
    TodoIngListResponse getCompleteTodos(Long folderId);
}
