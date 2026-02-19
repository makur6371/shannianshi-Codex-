package com.makur.ideaflash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Gpu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.makur.ideaflash.ai.AiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val aiService = remember { AiService(context) }
    val (apiKey, selectedModel) = remember { aiService.getSettings() }

    var apiKeyValue by remember { mutableStateOf(apiKey) }
    var selectedModelValue by remember { mutableStateOf(selectedModel) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Arrows, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AI 设置
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "AI 设置",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // API 密钥
                    OutlinedTextField(
                        value = apiKeyValue,
                        onValueChange = { apiKeyValue = it },
                        label = { Text("API 密钥") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholderText = "留空使用默认密钥"
                    )

                    // 模型选择
                    Text("选择模型", style = MaterialTheme.typography.bodyMedium)
                    val models = aiService.getAvailableModels()
                    var expanded by remember { mutableStateOf(false) }
                    val selectedText = selectedModelValue

                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedText,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor(),
                                colors = ExposedDropdownMenuDefaults.textFieldColors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                models.forEach { model ->
                                    DropdownMenuItem(
                                        text = { Text(model) },
                                        onClick = {
                                            selectedModelValue = model
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 保存按钮
                    Button(
                        onClick = {
                            aiService.saveSettings(apiKeyValue, selectedModelValue)
                            // 显示保存成功提示
                            // 这里可以添加 Snackbar 提示
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("保存设置")
                    }
                }
            }

            // 界面设置
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "界面设置",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // 主题颜色
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ColorLens, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("主题颜色", style = MaterialTheme.typography.bodyMedium)
                        }
                        // 这里可以添加颜色选择器
                        TextButton(onClick = { /* 打开颜色选择器 */ }) {
                            Text("更改")
                        }
                    }

                    // 纸张质感
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gpu, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("纸张质感", style = MaterialTheme.typography.bodyMedium)
                        }
                        Switch(
                            checked = true, // 从设置中读取
                            onCheckedChange = { /* 更新设置 */ }
                        )
                    }
                }
            }

            // 关于信息
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "关于",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("版本 1.0.0")
                    Text("作者：Makur")
                    Text("QQ：3397023886")
                    Text(
                        text = "GitHub：https://github.com/makur6371",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}