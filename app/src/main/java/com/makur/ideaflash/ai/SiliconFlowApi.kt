package com.makur.ideaflash.ai

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class Message(
    val role: String = "user",
    val content: String
)

data class ChatResponse(
    val id: String,
    val object_type: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

interface SiliconFlowService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chatCompletion(@Body request: ChatRequest): ChatResponse

    @GET("models")
    suspend fun getModels(): ModelsResponse
}

data class ModelsResponse(
    val object_type: String,
    val data: List<Model>
)

data class Model(
    val id: String,
    val object_type: String,
    val created: Long,
    val owned_by: String
)

class SiliconFlowApi {
    companion object {
        private const val BASE_URL = "https://api.siliconflow.cn/v1/"
        private const val DEFAULT_API_KEY = "sk-vupsrvyrpqcarruisjmqnhdjvpujjrnjhcztclamvxxtbowg"

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service: SiliconFlowService = retrofit.create(SiliconFlowService::class.java)

        fun createChatRequest(messages: List<Message>, model: String = "Qwen/Qwen2.5-7B-Instruct"): ChatRequest {
            return ChatRequest(
                model = model,
                messages = messages,
                temperature = 0.7,
                max_tokens = 1000
            )
        }
    }
}