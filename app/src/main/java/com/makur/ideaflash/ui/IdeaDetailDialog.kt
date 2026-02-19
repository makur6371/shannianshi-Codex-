package com.makur.ideaflash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analysis
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makur.ideaflash.data.Idea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailDialog(
    idea: Idea,
    onDismiss: () -> Unit,
    onEdit: (Idea) -> Unit,
    onDelete: (Idea) -> Unit,
    onAnalyze: (Idea) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("闪念详情")
                if (idea.aiAnalysis.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = { onAnalyze(idea) },
                        modifier = Modifier.size(width = 80.dp, height = 32.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("AI分析", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 内容
                OutlinedTextField(
                    value = idea.content,
                    onValueChange = { /* 只读 */ },
                    label = { Text("想法内容") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    singleLine = false,
                    minLines = 3,
                    maxLines = 10
                )

                // AI分析结果
                if (idea.aiAnalysis?.isNotBlank() == true) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Analysis,
                                    contentDescription = "AI分析",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text("AI分析结果", style = MaterialTheme.typography.titleSmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = idea.aiAnalysis,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 时间信息
                Text(
                    text = "创建时间: ${formatTimestamp(idea.timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = {
                        onEdit(idea)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("编辑")
                }
                TextButton(
                    onClick = {
                        onDelete(idea)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("删除")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("关闭")
            }
        }
    )
}

private fun formatTimestamp(timestamp: java.util.Date): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(timestamp)
}