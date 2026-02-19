package com.makur.ideaflash.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ideas")
data class Idea(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    var aiAnalysis: String? = null,
    var tags: String = "", // JSON格式存储
    var relatedIdeas: String = "", // JSON格式存储
    val color: String = "#87CEEB" // 默认天蓝色
)