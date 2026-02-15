package com.umc.timeto.todo.service;

import com.umc.timeto.folder.entity.Folder;
import com.umc.timeto.folder.repository.FolderRepository;
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
    private final FolderRepository folderRepository;

    @Override
    public TodoCreateResponse createTodo(Long memberId, Long folderId, TodoCreateRequest request) {

        Folder folder = folderRepository.findByFolderIdAndGoal_Member_MemberId(folderId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.FOLDER_NOT_FOUND)); // 권한도 NOT_FOUND로 숨김


        LocalTime duration = DurationParser.parseToLocalTime(request.getDuration());

        Todo todo = Todo.create(folder, request.getName(), request.getPriority(), duration);
        Todo saved = todoRepository.save(todo);

        return new TodoCreateResponse(saved.getTodoId());
    }
}
