package org.test.sioinc_ai_test.service


import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.test.sioinc_ai_test.constant.RoleType
import org.test.sioinc_ai_test.constant.SortByCreated
import org.test.sioinc_ai_test.dto.*
import org.test.sioinc_ai_test.entity.Chat
import org.test.sioinc_ai_test.entity.User
import org.test.sioinc_ai_test.model.ChatMessage
import org.test.sioinc_ai_test.model.ModelRequest
import org.test.sioinc_ai_test.model.ModelResponse
import org.test.sioinc_ai_test.repository.ChatRepository
import java.util.*

@Service
class ChatServiceImpl(
    @Value("\${ai.open-ai.key}") private val apiKey: String,
    private val userService: UserService,
    private val webClient: WebClient,
    private val cacheService: CacheService,
    private val chatRepository: ChatRepository
) : ChatService {

    companion object{
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun createChat(dto: ChatRequestDto): ChatResponseDto {
        // 대화 생성
        // 질문을 입력 받고 생성된 답변을 응답 합니다.
        // 질문의 필요한 파라미터
        // 질문 : String, 모델: string
        // 응답 : String 형태
        // 이때 Thread 불러오기 기능이 추가되어야 할거같은데
        // ConcurrentMap 으로 임시 구현 해보자

        // SecurityContext 에서 가져올거임
        val username: String = SecurityContextHolder.getContext().authentication.name as String

        val user = userService.loadUserByUsername(username) as UserWrapper

        val uuid: UUID = cacheService.getOrPutThreadUUID(user)

        val chatListByThreadId: List<Chat> = chatRepository.findAllByThreadId(uuid)

        val chatHistory: List<String> = getChatHistory(chatListByThreadId)

        val chattingWithModel: ModelResponse? = getChatWithModel(dto, chatHistory)

        chatRepository.save(Chat(
            threadId = uuid,
            userId = user.id,
            question = dto.question,
            answer = chattingWithModel.getAnswer(),
            timestamp = cacheService.getUuidTime(uuid)
        ))

        val chatResponseDto = ChatResponseDto(answer = chattingWithModel.getAnswer())

        return chatResponseDto

    }

    override fun createStreamingChat() {
        // 대화 생성
        // 질문을 입력 받고 생성된 답변을 응답 합니다.
        // 질문의 필요한 파라미터
        // 질문: String, 모델: string
        // 응답 Mono<String> 형태


    }

    override fun getAllThread(dto: ChatListRequestDto): ChatListResponseDto {

        //SecurityContext 로 추후 생성
        val username: String = SecurityContextHolder.getContext().authentication.name as String

        val user = userService.loadUserByUsername(username) as UserWrapper

        val pageable = PageRequest.of(
            dto.page,
            dto.size,
            if (dto.sort == SortByCreated.ACS) Sort.by("timestamp").ascending()
            else Sort.by("timestamp").descending()
        )

        val chatList: Page<Chat>? = if (user.role == RoleType.MEMBER){
             user.id?.let { chatRepository.findAllByUserId(userId = it, pageable = pageable) }
        }else{
             user.id?.let { chatRepository.findAll(pageable) }
        }

        val groupedChatList: Map<UUID?, List<Chat>> = chatListGroupedByThreadId(chatList)

        val responseDto = ChatListResponseDto(groupedChatList)

        return responseDto
    }

    private fun chatListGroupedByThreadId(chatList: Page<Chat>?): Map<UUID?, List<Chat>> {

        return chatList?.groupBy { it.threadId } ?: emptyMap()

    }

    override fun deleteThread() {
        TODO("Not yet implemented")
    }

    private fun getChatHistory(chatListByThreadId: List<Chat>): List<String> = chatListByThreadId.map { "Q: ${it.question} \n A: ${it.answer}" }

    private fun getChatWithModel(dto: ChatRequestDto, chatHistory: List<String>): ModelResponse? {

        val getMessages: List<ChatMessage> = makeMessageForm(chatHistory, dto.question)

        val request = ModelRequest(
            model = dto.model,
            message = getMessages
        )

        val result = webClient
            .post()
            .header(AUTHORIZATION_HEADER, "Bearer $apiKey")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ModelResponse::class.java)

        return result.block()
    }

    private fun makeMessageForm(chatHistory: List<String>, question: String): List<ChatMessage> {

        val historyList = chatHistory.toMutableList()

        historyList.add(question)

        return historyList.map { ChatMessage(content = it) }
    }

    private fun ModelResponse?.getAnswer(): String {

        val messages: List<ChatMessage> = this?.choices?.map { it.message }?: return ""

        return when {
            messages.isEmpty() -> ""
            messages.size > 1 -> messages.map { it.content }.joinToString { "\n" }
            else -> messages.map { it.content }.first()
        }

    }

}