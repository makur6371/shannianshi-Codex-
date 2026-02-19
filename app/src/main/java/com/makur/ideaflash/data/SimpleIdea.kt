package com.makur.ideaflash.data

import java.util.Date

data class SimpleIdea(
    val id: Long = 0,
    val content: String,
    val timestamp: Date = Date(),
    val color: String = "#87CEEB"
)