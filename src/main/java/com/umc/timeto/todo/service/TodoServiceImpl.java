package com.umc.timeto.todo.service;

import com.umc.timeto.folder.Folder;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.dto.request.TodoCreateRequest;
import com.umc.timeto.todo.dto.response.TodoCreateResponse;
import com.umc.timeto.todo.repository.TodoRepository;
import com.umc.timeto.todo.util.DurationParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public TodoCreateResponse createTodo(Long folderId, TodoCreateRequest request) {

        if (folderId == null || folderId <= 0) {
            // 프로젝트 ErrorCode에 맞게 교체
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        }

        LocalTime duration = DurationParser.parseToLocalTime(request.getDuration());

        Todo todo = Todo.create(folderId, request.getName(), request.getPriority(), duration);
        Todo saved = todoRepository.save(todo);

        return new TodoCreateResponse(saved.getTodoId());
    }
}
