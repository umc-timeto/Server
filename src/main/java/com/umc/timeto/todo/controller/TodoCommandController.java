package com.umc.timeto.todo.controller;

import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import com.umc.timeto.todo.dto.request.TodoStatusUpdateRequest;
import com.umc.timeto.todo.dto.response.TodoStatusUpdateResponse;
import com.umc.timeto.todo.service.TodoCommandService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoCommandController {
//    private final TodoCommandService todoCommandService;
//
//    @Operation(summary = "할 일 상태 변경", description = "todoId로 할 일 상태를 변경합니다.")
//    @PatchMapping("/status/{todoId}")
//    public ResponseDTO<TodoStatusUpdateResponse> updateTodoStatus(
//            @PathVariable Long todoId,
//            @RequestBody @Valid TodoStatusUpdateRequest request
//    ) {
//        TodoStatusUpdateResponse data = todoCommandService.updateStatus(todoId, request);
//        return new ResponseDTO<>(ResponseCode.COMMON200, data);
//    }
}
