package com.makur.ideaflash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Arrows
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.makur.ideaflash.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdeaFlashApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaFlashApp() {
    val navController = rememberNavController()

    MaterialTheme(
        colorScheme = if (isPaperTextureEnabled()) {
            // 纸张质感主题
            val colors = lightColorScheme(
                primary = Color(0xFF87CEEB), // 天蓝色
                onPrimary = Color.White,
                secondary = Color(0xFF7C4DFF), // 紫色
                onSecondary = Color.White,
                background = Color(0xFFFAFAFA), // 米白色
                surface = Color(0xFFFFFEF7), // 纸张白色
                onSurface = Color(0xFF212121)
            )
            colors
        } else {
            // 纯色主题
            lightColorScheme()
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = "ideas_screen"
            ) {
                composable("ideas_screen") {
                    IdeasScreen(navController)
                }
                composable("network_screen") {
                    NetworkScreen(navController)
                }
                composable("settings_screen") {
                    SettingsScreen(navController)
                }
                composable("about_screen") {
                    AboutScreen(navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeasScreen(navController: NavController) {
    val viewModel: IdeaViewModel = viewModel()
    val ideas by viewModel.ideas.collectAsState()
    val context = LocalContext.current
    val aiService = remember { AiService(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("闪念式") },
                actions = {
                    IconButton(onClick = { navController.navigate("network_screen") }) {
                        Icon(Icons.Default.NetworkPing, contentDescription = "闪念网络")
                    }
                    IconButton(onClick = { navController.navigate("settings_screen") }) {
                        Icon(Icons.Default.Menu, contentDescription = "设置")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加闪念")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (ideas.isEmpty()) {
                EmptyState()
            } else {
                IdeaList(ideas = ideas, onIdeaClick = { idea ->
                    viewModel.showIdeaDetail(idea)
                }, onAnalyzeClick = { idea ->
                    viewModel.analyzeWithAI(idea, aiService)
                })
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "暂无闪念",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = "点击右下角按钮添加你的第一个想法",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// 检查是否启用了纸张质感
@Composable
private fun isPaperTextureEnabled(): Boolean {
    // 这里可以从设置中读取配置
    return false
}