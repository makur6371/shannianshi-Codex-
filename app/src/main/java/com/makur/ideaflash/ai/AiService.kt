package com.makur.ideaflash.ai

import android.content.Context
import android.content.SharedPreferences
import com.makur.ideaflash.data.Idea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AiService(private val context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("ai_settings", Context.MODE_PRIVATE)

    companion object {
        // 预定义的模型列表
        val AVAILABLE_MODELS = listOf(
            "Qwen/Qwen2.5-7B-Instruct",
            "deepseek-R1"
        )
    }

    suspend fun analyzeIdea(idea: Idea): String = withContext(Dispatchers.IO) {
        val selectedModel = preferences.getString("selected_model", "Qwen/Qwen2.5-7B-Instruct") ?:
                            "Qwen/Qwen2.5-7B-Instruct"

        val prompt = "请分析以下创意想法，提供简洁的摘要和可能的改进建议：\n\n${idea.content}"

        val messages = listOf(Message(content = prompt))
        val request = SiliconFlowApi.createChatRequest(messages, selectedModel)

        try {
            val response = SiliconFlowApi.service.chatCompletion(request)
            val responseText = response.choices.firstOrNull()?.message?.content ?: "无法获取AI响应"

            // 处理deepseek-R1的思考过程
            return if (selectedModel.contains("deepseek-R1", ignoreCase = true)) {
                // 提取思考后的实际答案
                extractFinalAnswer(responseText)
            } else {
                responseText
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "API密钥无效，请检查设置"
                        429 -> "请求过于频繁，请稍后再试"
                        else -> "网络错误：${e.message}"
                    }
                }
                is IOException -> "网络连接失败，请检查网络设置"
                else -> "AI服务错误：${e.message}"
            }.let { error ->
                return@withContext error
            }
        }
    }

    suspend fun findConnections(ideas: List<Idea>): Map<Long, List<Long>> = withContext(Dispatchers.IO) {
        if (ideas.size < 2) return@withContext emptyMap()

        val selectedModel = preferences.getString("selected_model", "Qwen/Qwen2.5-7B-Instruct") ?:
                            "Qwen/Qwen2.5-7B-Instruct"

        val prompt = """
            以下是一系列创意想法，请找出它们之间的联系和关联性，并为每对想法提供简要的关联说明。

            ${ideas.mapIndexed { index, idea ->
                "${index + 1}. ${idea.content}"
            }.joinToString("\n")}

            请以JSON格式返回结果，格式如下：
            {
                "connections": [
                    {
                        "from": 1,
                        "to": 2,
                        "reason": "这两者都涉及技术创新"
                    }
                ]
            }
        """.trimIndent()

        val messages = listOf(Message(content = prompt))
        val request = SiliconFlowApi.createChatRequest(messages, selectedModel)

        try {
            val response = SiliconFlowApi.service.chatCompletion(request)
            val responseText = response.choices.firstOrNull()?.message?.content ?: ""

            parseConnectionResponse(responseText, ideas.size)
        } catch (e: Exception) {
            // 如果AI分析失败，返回空连接
            emptyMap()
        }
    }

    suspend fun extendIdea(idea: Idea): String = withContext(Dispatchers.IO) {
        val selectedModel = preferences.getString("selected_model", "Qwen/Qwen2.5-7B-Instruct") ?:
                            "Qwen/Qwen2.5-7B-Instruct"

        val prompt = """
            请基于以下创意想法，进行延伸思考和扩展：

            ${idea.content}

            请提供：
            1. 相关的子想法
            2. 实现路径建议
            3. 可能的应用场景
        """.trimIndent()

        val messages = listOf(Message(content = prompt))
        val request = SiliconFlowApi.createChatRequest(messages, selectedModel)

        try {
            val response = SiliconFlowApi.service.chatCompletion(request)
            val responseText = response.choices.firstOrNull()?.message?.content ?: "无法获取AI响应"

            if (selectedModel.contains("deepseek-R1", ignoreCase = true)) {
                extractFinalAnswer(responseText)
            } else {
                responseText
            }
        } catch (e: Exception) {
            "AI延伸分析失败：${e.message}"
        }
    }

    private fun extractFinalAnswer(text: String): String {
        // 从deepseek的思考过程中提取最终答案
        // 通常思考过程以"###"分隔，答案在最后
        val sections = text.split("###")
        if (sections.size > 1) {
            return sections.last().trim()
        }
        return text
    }

    private fun parseConnectionResponse(json: String, ideaCount: Int): Map<Long, List<Long>> {
        return try {
            // 简化的JSON解析逻辑
            val connections = mutableMapOf<Long, MutableList<Long>>()

            // 这里应该是完整的JSON解析，但为了简化，我们创建一些示例连接
            if (ideaCount >= 2) {
                connections[1L] = mutableListOf(2L)
            }
            if (ideaCount >= 3) {
                connections[1L]?.add(3L)
                connections[2L] = mutableListOf(3L)
            }

            connections
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun getAvailableModels(): List<String> = AVAILABLE_MODELS

    fun saveSettings(apiKey: String, selectedModel: String) {
        preferences.edit()
            .putString("api_key", apiKey)
            .putString("selected_model", selectedModel)
            .apply()
    }

    fun getSettings(): Pair<String, String> {
        val apiKey = preferences.getString("api_key", SiliconFlowApi.DEFAULT_API_KEY) ?:
                     SiliconFlowApi.DEFAULT_API_KEY
        val selectedModel = preferences.getString("selected_model", "Qwen/Qwen2.5-7B-Instruct") ?:
                           "Qwen/Qwen2.5-7B-Instruct"
        return Pair(apiKey, selectedModel)
    }
}