package com.umc.timeto.todo.service;
import com.umc.timeto.block.service.BlockService;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;
import com.umc.timeto.todo.repository.TodoRepository;
import com.umc.timeto.todo.util.DurationFormatter;
import com.umc.timeto.todo.util.DurationParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoCommandServiceImpl implements TodoCommandService{
    private final TodoRepository todoRepository;
    private final BlockService blockService;

    @Override
    public TodoStatusUpdateResponse updateStatus(Long memberId,Long todoId, TodoStatusUpdateRequest request) {

        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId,memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND)); // 너희 ErrorCode로 교체

        todo.changeState(request.getState());

        return new TodoStatusUpdateResponse(
                todo.getTodoId(),
                todo.getState().name() // "COMPLETE"
        );
    }
    @Override
    public TodoGetResponse updateTodo(Long memberId, Long todoId, TodoUpdateRequest request) {

        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        // ✅ 부분 업데이트: 들어온 것만 반영
        if (request.getName() != null && !request.getName().isBlank()) {
            todo.changeName(request.getName());
        }

        if (request.getPriority() != null) {
            todo.changePriority(request.getPriority());
        }
        // duration 변경 시 Block이 처리
        if (request.getDuration() != null && !request.getDuration().isBlank()) {
            LocalTime parsed = DurationParser.parseToLocalTime(request.getDuration());
            blockService.updateBlockDurationByTodo(todoId, memberId, parsed);
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
    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        boolean exists = todoRepository.existsByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId);
        if (!exists) {
            throw new GlobalException(ErrorCode.TODO_NOT_FOUND);
        }
        todoRepository.deleteByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId);
    }


    @Transactional(readOnly = true)
    @Override
    public List<TodoGetResponse> getUnblockedTodos(Long memberId,Long folderId) {

        List<Todo> todos =
                todoRepository
                        .findByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndBlockIsNull(
                                folderId,
                                memberId
                        );

        return todos.stream()
                .map(todo -> new TodoGetResponse(
                        todo.getTodoId(),
                        todo.getName(),
                        DurationFormatter.format(todo.getDuration()),
                        todo.getPriority(),
                        todo.getState()
                ))
                .toList();
    }

}
