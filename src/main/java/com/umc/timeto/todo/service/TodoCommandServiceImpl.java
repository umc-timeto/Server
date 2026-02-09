package com.umc.timeto.todo.service;

import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umc.timeto.todo.util.DurationFormatter;
import com.umc.timeto.todo.util.DurationParser;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoCommandServiceImpl implements TodoCommandService{
    private final TodoRepository todoRepository;

    @Override
    public TodoStatusUpdateResponse updateStatus(Long todoId, TodoStatusUpdateRequest request) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND)); // 너희 ErrorCode로 교체

        todo.changeState(request.getState());

        return new TodoStatusUpdateResponse(
                todo.getTodoId(),
                todo.getState().name() // "COMPLETE"
        );
    }
    @Override
    public TodoGetResponse updateTodo(Long todoId, TodoUpdateRequest request) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND));

        // ✅ 부분 업데이트: 들어온 것만 반영
        if (request.getName() != null && !request.getName().isBlank()) {
            todo.changeName(request.getName());
        }

        if (request.getPriority() != null) {
            todo.changePriority(request.getPriority());
        }

        if (request.getDuration() != null && !request.getDuration().isBlank()) {
            LocalTime parsed = DurationParser.parseToLocalTime(request.getDuration());
            todo.changeDuration(parsed);
        }

        return new TodoGetResponse(
                todo.getTodoId(),
                todo.getName(),
                DurationFormatter.format(todo.getDuration()),
                todo.getPriority(),
                todo.getState()
        );
    }
    @Override
    public void deleteTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND));

        todoRepository.delete(todo);
        // 또는 todoRepository.deleteById(todoId); (존재 검사 없이 바로 삭제하고 싶으면)
    }
}
