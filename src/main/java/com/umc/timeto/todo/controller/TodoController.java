package com.umc.timeto.todo.controller;

import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import com.umc.timeto.todo.dto.request.TodoCreateRequest;
import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.request.TodoUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoCreateResponse;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.dto.response.TodoIngListResponse;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;
import com.umc.timeto.todo.service.TodoCommandService;
import com.umc.timeto.todo.service.TodoQueryService;
import com.umc.timeto.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    private final TodoCommandService todoCommandService;

    private final TodoQueryService todoQueryService;

    private Long getMemberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }

    @Operation(summary = "할 일 정보 조회", description = "todoId로 할 일 상세 정보를 조회합니다.")
    @GetMapping("/{todoId}")
    public ResponseDTO<TodoGetResponse> getTodo(@PathVariable Long todoId) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        TodoGetResponse data = todoQueryService.getTodo(memberId, todoId);
        return new ResponseDTO<>(ResponseCode.COMMON200, data);
    }

    @Operation(summary = "할 일 상태 변경", description = "todoId로 할 일 상태를 변경합니다.")
    @PatchMapping("/status/{todoId}")
    public ResponseDTO<TodoStatusUpdateResponse> updateTodoStatus(
            @PathVariable Long todoId,
            @RequestBody @Valid TodoStatusUpdateRequest request
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        TodoStatusUpdateResponse data = todoCommandService.updateStatus(memberId, todoId, request);
        return new ResponseDTO<>(ResponseCode.COMMON200, data);
    }

    @Operation(summary = "할 일 추가", description = "폴더에 할 일을 추가합니다.")
    @PostMapping("/{folderId}")
    public ResponseDTO<TodoCreateResponse> createTodo(
            @PathVariable Long folderId,
            @RequestBody @Valid TodoCreateRequest request
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        TodoCreateResponse data = todoService.createTodo(memberId, folderId, request);

        // TODO_CREATED가 없으면 COMMON200으로 바꿔도 됨
        return new ResponseDTO<>(ResponseCode.COMMON200, data);
    }

    @Operation(summary = "할 일 수정", description = "todoId로 할 일 정보를 수정합니다.")
    @PatchMapping("/{todoId}")
    public ResponseDTO<TodoGetResponse> updateTodo(
            @PathVariable Long todoId,
            @RequestBody @Valid TodoUpdateRequest request
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        TodoGetResponse result = todoCommandService.updateTodo(memberId, todoId, request);
        return new ResponseDTO<>(ResponseCode.COMMON200, result);
    }

    @Operation(summary = "진행 중인 할 일 리스트", description = "folderId로 해당 폴더에 진행중인 할 일을 조회합니다.")
    @GetMapping("/{folderId}/todo/progress")
    public ResponseDTO<TodoIngListResponse> getInProgressTodos(@PathVariable Long folderId) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        TodoIngListResponse result = todoQueryService.getInProgressTodos(memberId, folderId);
        return new ResponseDTO<>(ResponseCode.COMMON200, result);
    }

    @Operation(summary = "완료된 할 일 리스트", description = "folderId로 해당 폴더에 완료된 할 일을 조회합니다.")
    @GetMapping("/{folderId}/todo/complete")
    public ResponseDTO<TodoIngListResponse> getCompleteTodos(@PathVariable Long folderId) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        TodoIngListResponse result = todoQueryService.getCompleteTodos(memberId, folderId);
        return new ResponseDTO<>(ResponseCode.COMMON200, result);
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/{todoId}")
    public ResponseDTO<String> deleteTodo(@PathVariable Long todoId) {
        Long memberId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        todoCommandService.deleteTodo(memberId, todoId);
        return new ResponseDTO<>(ResponseCode.COMMON200, "성공");
    }

    @Operation(summary = "블록 생성 후보 할일 조회", description = "블록이 등록되지 않은 할 일을 조회합니다")
    @GetMapping("/{folderId}/todo/unblocked")
    public ResponseEntity<ResponseDTO<List<TodoGetResponse>>> getUnblockedTodos(
            @PathVariable Long folderId,
            Authentication authentication
    ) {

        Long memberId = getMemberId(authentication);

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        ResponseCode.SUCCESS_GET_UNBLOCKED_TODOS,
                        todoCommandService.getUnblockedTodos(memberId,folderId)
                )
        );
    }

}
