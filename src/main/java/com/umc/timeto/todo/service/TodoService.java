package com.umc.timeto.todo.service;

import com.umc.timeto.todo.dto.request.TodoCreateRequest;
import com.umc.timeto.todo.dto.response.TodoCreateResponse;

public interface TodoService {
    TodoCreateResponse createTodo(Long folderId, TodoCreateRequest request);
}
