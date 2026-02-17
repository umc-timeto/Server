package com.umc.timeto.todo.service;
import com.umc.timeto.block.service.BlockService;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.todo.domain.Todo;
import com.umc.timeto.todo.domain.enums.TodoState;
import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoOrderUpdateResponse;
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
    public TodoStatusUpdateResponse updateStatus(Long memberId, Long todoId, TodoStatusUpdateRequest request) {

        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        TodoState fromState = todo.getState();
        TodoState toState = request.getState();

        if (fromState != toState) {

            Long folderId = todo.getFolder().getFolderId();

            // 1️⃣ 상태 먼저 변경
            todo.changeState(toState);

            // 2️⃣ 기존 state 정렬 재정립
            List<Todo> fromList =
                    todoRepository.findAllByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndStateOrderBySortOrderAsc(
                            folderId, memberId, fromState
                    );

            int order = 1;
            for (Todo t : fromList) {
                t.changeSortOrder(order++);
            }

            // 3️⃣ 새 state에서 맨 아래 배치
            List<Todo> toList =
                    todoRepository.findAllByFolder_FolderIdAndFolder_Goal_Member_MemberIdAndStateOrderBySortOrderAsc(
                            folderId, memberId, toState
                    );

            todo.changeSortOrder(toList.size());
        }

        return new TodoStatusUpdateResponse(todo.getTodoId(), todo.getState().name());
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
                todo.getState(),
                todo.getStartAt(),
                todo.getSortOrder()
        );
    }
    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        Long folderId = todo.getFolder().getFolderId();
        TodoState state = todo.getState();
        int deletedOrder = todo.getSortOrder();

        todoRepository.delete(todo); // 또는 기존 deleteBy...
        todoRepository.pullUpAfterDelete(folderId, memberId, state, deletedOrder);
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
                        todo.getState(),
                        todo.getStartAt(),
                        todo.getSortOrder()
                ))
                .toList();
    }

    @Transactional
    public TodoOrderUpdateResponse updateTodoOrder(Long memberId, Long todoId, int targetOrder) {
        Todo todo = todoRepository.findByTodoIdAndFolder_Goal_Member_MemberId(todoId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));

        Long folderId = todo.getFolder().getFolderId();
        TodoState state = todo.getState();

        int currentOrder = todo.getSortOrder();

        int max = todoRepository.findMaxSortOrder(folderId, memberId, state);
        int clamped = Math.max(1, Math.min(targetOrder, max));

        if (clamped == currentOrder) {
            return new TodoOrderUpdateResponse(todoId, currentOrder);
        }

        if (clamped < currentOrder) {
            todoRepository.shiftDownForMoveUp(folderId, memberId, state, clamped, currentOrder);
        } else {
            todoRepository.shiftUpForMoveDown(folderId, memberId, state, currentOrder, clamped);
        }

        // ✅ bulk update 이후에는 다시 조회해서 안전하게 반영
        Todo refreshed = todoRepository.findById(todoId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TODO_NOT_FOUND));
        refreshed.changeSortOrder(clamped);

        return new TodoOrderUpdateResponse(todoId, clamped);
    }



}
