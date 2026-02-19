package com.makur.ideaflash.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IdeaRepository(private val context: Context) {
    private val ideaDao = IdeaDatabase.getDatabase(context).ideaDao()
    private val _ideas = MutableLiveData<List<Idea>>()
    val ideas: LiveData<List<Idea>> = _ideas

    init {
        _ideas.value = emptyList()
    }

    suspend fun getAllIdeas(): List<Idea> = withContext(Dispatchers.IO) {
        ideaDao.getAllIdeas().first()
    }

    suspend fun getIdeaById(id: Long): Idea? = withContext(Dispatchers.IO) {
        ideaDao.getIdeaById(id)
    }

    suspend fun insertIdea(idea: Idea): Long = withContext(Dispatchers.IO) {
        ideaDao.insertIdea(idea)
    }

    suspend fun updateIdea(idea: Idea) = withContext(Dispatchers.IO) {
        ideaDao.updateIdea(idea)
    }

    suspend fun deleteIdea(idea: Idea) = withContext(Dispatchers.IO) {
        ideaDao.deleteIdea(idea)
    }

    suspend fun searchIdeas(query: String): List<Idea> = withContext(Dispatchers.IO) {
        ideaDao.searchIdeas(query).first()
    }

    suspend fun getRelatedIdeas(ideaId: Long): List<Idea> {
        val idea = getIdeaById(ideaId)
        return idea?.relatedIdeas?.let { ids ->
            ideaDao.getIdeasByIds(ids).first()
        } ?: emptyList()
    }
}