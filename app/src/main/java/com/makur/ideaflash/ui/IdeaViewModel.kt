package com.makur.ideaflash.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makur.ideaflash.ai.AiService
import com.makur.ideaflash.data.Idea
import com.makur.ideaflash.data.IdeaRepository
import kotlinx.coroutines.launch

class IdeaViewModel(private val repository: IdeaRepository) : ViewModel() {
    var ideas by mutableStateOf<List<Idea>>(emptyList())
        private set

    var showAddDialog by mutableStateOf(false)
        private set

    var showIdeaDetail by mutableStateOf<Idea?>(null)
        private set

    var showAnalyzeDialog by mutableStateOf(false)
        private set

    var currentIdea by mutableStateOf<Idea?>(null)
        private set

    var aiAnalysisResult by mutableStateOf<String?>(null)
        private set

    var isAnalyzing by mutableStateOf(false)
        private set

    init {
        loadIdeas()
    }

    private fun loadIdeas() {
        viewModelScope.launch {
            repository.getAllIdeas().collect { ideaList ->
                ideas = ideaList
            }
        }
    }

    fun showAddDialog() {
        showAddDialog = true
    }

    fun hideAddDialog() {
        showAddDialog = false
    }

    fun addIdea(content: String, color: String = "#87CEEB") {
        if (content.isBlank()) return

        viewModelScope.launch {
            val idea = Idea(
                content = content,
                color = color,
                timestamp = System.currentTimeMillis()
            )
            repository.insertIdea(idea)
            hideAddDialog()
        }
    }

    fun showIdeaDetail(idea: Idea) {
        currentIdea = idea
        showIdeaDetail = true
    }

    fun hideIdeaDetail() {
        showIdeaDetail = false
        currentIdea = null
        aiAnalysisResult = null
    }

    fun analyzeWithAI(idea: Idea, aiService: AiService) {
        currentIdea = idea
        isAnalyzing = true
        showAnalyzeDialog = true

        viewModelScope.launch {
            try {
                val analysis = aiService.analyzeIdea(idea)
                aiAnalysisResult = analysis
            } catch (e: Exception) {
                aiAnalysisResult = "AI分析失败：${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun hideAnalyzeDialog() {
        showAnalyzeDialog = false
        currentIdea = null
        aiAnalysisResult = null
        isAnalyzing = false
    }

    fun deleteIdea(idea: Idea) {
        viewModelScope.launch {
            repository.deleteIdea(idea)
            if (currentIdea?.id == idea.id) {
                hideIdeaDetail()
            }
        }
    }

    fun updateIdea(idea: Idea, newContent: String) {
        if (newContent.isBlank()) return

        viewModelScope.launch {
            val updatedIdea = idea.copy(content = newContent)
            repository.updateIdea(updatedIdea)

            // 如果正在查看这个想法，更新当前显示的内容
            if (currentIdea?.id == idea.id) {
                currentIdea = updatedIdea
            }
        }
    }

    fun searchIdeas(query: String) {
        if (query.isBlank()) {
            loadIdeas()
            return
        }

        viewModelScope.launch {
            repository.searchIdeas(query).collect { results ->
                ideas = results
            }
        }
    }
}