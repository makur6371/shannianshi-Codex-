package com.makur.ideaflash.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Query("SELECT * FROM ideas ORDER BY timestamp DESC")
    fun getAllIdeas(): Flow<List<Idea>>

    @Query("SELECT * FROM ideas WHERE id = :id")
    suspend fun getIdeaById(id: Long): Idea?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: Idea): Long

    @Update
    suspend fun updateIdea(idea: Idea)

    @Delete
    suspend fun deleteIdea(idea: Idea)

    @Query("SELECT * FROM ideas WHERE content LIKE :query OR tags LIKE :query")
    fun searchIdeas(query: String): Flow<List<Idea>>

    @Query("SELECT * FROM ideas WHERE timestamp >= :date")
    fun getIdeasFrom(date: Long): Flow<List<Idea>>

    @Transaction
    @Query("SELECT * FROM ideas WHERE id IN (:ideaIds)")
    fun getIdeasByIds(ideaIds: List<Long>): Flow<List<Idea>>
}