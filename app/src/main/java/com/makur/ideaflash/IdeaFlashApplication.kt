package com.makur.ideaflash

import android.app.Application
import com.makur.ideaflash.data.IdeaRepository

class IdeaFlashApplication : Application() {
    val repository: IdeaRepository by lazy {
        IdeaRepository(this)
    }
}