package com.makur.ideaflash.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Arrows
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.makur.ideaflash.data.Idea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(navController: NavController) {
    val viewModel: IdeaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val ideas by viewModel.ideas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("闪念网络") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Arrows, contentDescription = "返回")
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
                NetworkView(
                    ideas = ideas,
                    onIdeaClick = { idea ->
                        viewModel.showIdeaDetail(idea)
                    }
                )
            }
        }
    }
}

@Composable
fun NetworkView(
    ideas: List<Idea>,
    onIdeaClick: (Idea) -> Unit,
    modifier: Modifier = Modifier
) {
    val nodes = remember(ideas) {
        ideas.mapIndexed { index, idea ->
            Node(
                id = index,
                idea = idea,
                position = calculateNodePosition(index, ideas.size)
            )
        }
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        // 绘制连接线
        nodes.forEach { node ->
            node.connections.forEach { targetId ->
                val target = nodes.find { it.id == targetId }
                if (target != null) {
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(node.position.x, node.position.y),
                        end = Offset(target.position.x, target.position.y),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
    }

    // 绘制节点
    nodes.forEach { node ->
        NetworkNode(
            node = node,
            onClick = { onIdeaClick(node.idea) },
            modifier = Modifier.offset(
                x = node.position.x,
                y = node.position.y
            )
        )
    }
}

@Composable
fun NetworkNode(
    node: Node,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(node.idea.color)).copy(alpha = 0.1f),
            contentColor = Color(android.graphics.Color.parseColor(node.idea.color))
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 简短显示想法内容
            Text(
                text = node.idea.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 如果已AI分析，显示标记
            if (node.idea.aiAnalysis.isNullOrBlank().not()) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "已分析",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

data class Node(
    val id: Int,
    val idea: Idea,
    val position: Offset,
    val connections: List<Int> = emptyList()
)

private fun calculateNodePosition(index: Int, total: Int): Offset {
    val centerX = 200f
    val centerY = 300f
    val radius = 150f

    if (total == 1) {
        return Offset(centerX, centerY)
    }

    val angle = (2 * Math.PI * index / total).toFloat()
    val x = centerX + radius * Math.cos(angle.toDouble()).toFloat()
    val y = centerY + radius * Math.sin(angle.toDouble()).toFloat()

    return Offset(x, y)
}