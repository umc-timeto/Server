package com.umc.timeto.todo.controller;


import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import com.umc.timeto.todo.dto.response.TodoGetResponse;
import com.umc.timeto.todo.service.TodoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoQueryController {
//    private final TodoQueryService todoQueryService;
//
//    @Operation(summary = "할 일 정보 조회", description = "todoId로 할 일 상세 정보를 조회합니다.")
//    @GetMapping("/{todoId}")
//    public ResponseDTO<TodoGetResponse> getTodo(@PathVariable Long todoId) {
//        TodoGetResponse data = todoQueryService.getTodo(todoId);
//        return new ResponseDTO<>(ResponseCode.COMMON200, data);
//    }
}
