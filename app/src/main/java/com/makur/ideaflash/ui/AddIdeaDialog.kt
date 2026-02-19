package com.makur.ideaflash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.makur.ideaflash.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIdeaDialog(
    onDismiss: () -> Unit,
    onConfirm: (content: String, color: String) -> Unit
) {
    var ideaContent by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#87CEEB") }

    val colorOptions = listOf(
        "#87CEEB", // 天蓝色
        "#1E88E5", // 科技蓝
        "#7C4DFF", // 科技紫
        "#FFB74D", // 橙黄色
        "#8D6E63", // 棕色
        "#424242", // 墨灰色
        "#80CBC4"  // 奶绿色
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加闪念") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 输入框
                OutlinedTextField(
                    value = ideaContent,
                    onValueChange = { ideaContent = it },
                    label = { Text("输入你的想法...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 3,
                    maxLines = 5
                )

                // 颜色选择器
                Text("选择颜色", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorOptions.forEach { color ->
                        ColorOption(
                            color = color,
                            isSelected = selectedColor == color,
                            onClick = { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (ideaContent.isNotBlank()) {
                        onConfirm(ideaContent, selectedColor)
                    }
                },
                enabled = ideaContent.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun ColorOption(
    color: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        },
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(color))
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "已选择",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}