package com.umc.timeto.todo.service;

import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoIngListResponse;

public interface TodoQueryService {
    TodoGetResponse getTodo(Long memberId, Long todoId);
    TodoIngListResponse getInProgressTodos(Long memberId, Long folderId);
    TodoIngListResponse getCompleteTodos(Long memberId, Long folderId);
}
