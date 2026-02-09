package com.umc.timeto.todo.service;

import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.enums.TodoState;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.dto.response.TodoIngListResponse;
import com.umc.timeto.todo.repository.TodoRepository;
import com.umc.timeto.todo.util.DurationFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {
    private final TodoRepository todoRepository;

    @Override
    public TodoGetResponse getTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND)); // 너희 ErrorCode로 교체

        return new TodoGetResponse(
                todo.getTodoId(),
                todo.getName(),
                DurationFormatter.format(todo.getDuration()),
                todo.getPriority(),
                todo.getState()
        );
    }
    @Override
    public TodoIngListResponse getInProgressTodos(Long folderId) {

        List<Todo> todos = todoRepository.findAllByFolderIdAndStateOrderByTodoIdDesc(folderId, TodoState.progress);

        List<TodoIngListResponse.TodoIngItem> items = todos.stream()
                .map(t -> TodoIngListResponse.TodoIngItem.builder()
                        .todoId(t.getTodoId())
                        .name(t.getName())
                        .priority(t.getPriority())
                        .duration(DurationFormatter.format(t.getDuration()))
                        .build())
                .toList();

        return TodoIngListResponse.builder()
                .count(items.size())
                .todos(items)
                .build();
    }

    @Override
    public TodoIngListResponse getCompleteTodos(Long folderId) {
        // ✅ enum이 COMPLETE면 TodoState.COMPLETE 로 변경
        List<Todo> todos = todoRepository.findAllByFolderIdAndStateOrderByTodoIdDesc(folderId, TodoState.complete);

        List<TodoIngListResponse.TodoIngItem> items = todos.stream()
                .map(t -> TodoIngListResponse.TodoIngItem.builder()
                        .todoId(t.getTodoId())
                        .name(t.getName())
                        .priority(t.getPriority())
                        .duration(DurationFormatter.format(t.getDuration()))
                        .build())
                .toList();

        return TodoIngListResponse.builder()
                .count(items.size())
                .todos(items)
                .build();
    }

}
