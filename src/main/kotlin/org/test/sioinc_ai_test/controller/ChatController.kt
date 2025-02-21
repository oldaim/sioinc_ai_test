package org.test.sioinc_ai_test.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
        @RequestBody(required = true) model: String
    ): ResponseEntity<ChatResponseDto> {


        val requestDto = ChatRequestDto(
            model = model,
            question = question
        )

       val result = chatService.createChat(requestDto)

       return ResponseEntity.ok().body(result)

    }

    @GetMapping("/{id}")
    fun getChatThread(){
        TODO("조회 목록")
    }

    @DeleteMapping("/delete")
    fun deleteChatThread(){
        TODO("삭제 목록")
    }

}