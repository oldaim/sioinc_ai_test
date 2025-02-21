package org.test.sioinc_ai_test.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.test.sioinc_ai_test.constant.SortByCreated
import org.test.sioinc_ai_test.dto.ChatListRequestDto
import org.test.sioinc_ai_test.dto.ChatListResponseDto
import org.test.sioinc_ai_test.dto.ChatRequestDto
import org.test.sioinc_ai_test.dto.ChatResponseDto
import org.test.sioinc_ai_test.service.ChatService

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/create")
    fun <T> createChat(
        @RequestBody(required = true) question: String,
        @RequestBody(required = false) model: String = "gpt-4o",
        @RequestBody(required = false) isStreaming: Boolean = false
    ): ResponseEntity<ChatResponseDto> {


        val requestDto = ChatRequestDto(
            model = model,
            question = question
        )

       val result = chatService.createChat(requestDto)

       return ResponseEntity.ok().body(result)

    }

    @GetMapping("/list")
    fun getChatThread(
        @RequestParam("page", required = true) page: Int,
        @RequestParam("size", required = true) size: Int,
        @RequestParam("sort", required = true) direction: SortByCreated
    ): ResponseEntity<ChatListResponseDto> {

        val requestDto = ChatListRequestDto(
            page = page,
            size = size,
            sort = direction
        )

        val result = chatService.getAllThread(requestDto)

        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/delete")
    fun deleteChatThread(){
        TODO("삭제 목록")
    }

}