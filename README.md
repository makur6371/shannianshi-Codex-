# 闪念式 - Android创意记录APP

## 项目简介
"闪念式"是一个专为创意工作者设计的Android应用，可以随时随地记录灵感想法，并通过AI进行深度分析。

## 功能特色
- 📝 **随时随地记录**：快速捕捉迸发的创意想法
- 🤖 **AI分析**：使用硅基流动AI服务分析想法
- 🌐 **闪念网络**：可视化展示想法之间的关联
- 🎨 **简洁界面**：采用纸张质感设计，简约而不简单
- 🎨 **多彩主题**：提供多种纯色主题选择

## 技术栈
- **开发语言**：Kotlin
- **UI框架**：Jetpack Compose
- **数据库**：Room
- **网络**：Retrofit + Gson
- **AI服务**：硅基流动API

## 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- Android SDK API 34 (Android 14)
- Kotlin 1.9.10
- Gradle 8.1.2

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/makur6371/idea-flash.git
cd idea-flash
```

### 2. 打开项目
使用Android Studio打开项目文件夹

### 3. 配置API
默认已配置硅基流动API：
- 基础URL: `https://api.siliconflow.cn/v1`
- API密钥: `sk-vupsrvyrpqcarruisjmqnhdjvpujjrnjhcztclamvxxtbowg`
- 默认模型: `Qwen/Qwen2.5-7B-Instruct`

### 4. 构建和运行
1. 连接Android设备或启动模拟器
2. 点击Android Studio的绿色运行按钮
3. 或使用命令行：
```bash
./gradlew installDebug
```

## 项目结构
```
app/
├── src/main/
│   ├── java/com/makur/ideaflash/
│   │   ├── data/          # 数据模型和数据库
│   │   ├── ai/            # AI服务相关
│   │   └── ui/            # UI组件和界面
│   ├── res/
│   │   ├── drawable/      # 图片资源
│   │   ├── values/        # 颜色、尺寸等资源
│   │   └── xml/          # 配置文件
│   └── AndroidManifest.xml
├── build.gradle           # 应用级构建配置
└── proguard-rules.pro    # 代码混淆规则
```

## 使用说明

### 基本操作
1. **添加想法**：点击右下角FloatingActionButton
2. **查看详情**：点击想法卡片
3. **AI分析**：在详情页点击"AI分析"按钮
4. **查看网络**：点击顶部网络图标
5. **设置**：点击顶部设置图标

### AI功能
- **分析想法**：对创意进行深度分析，提供改进建议
- **连接想法**：自动发现想法间的关联
- **延伸思考**：基于现有想法进行扩展

### 主题设置
- 支持多种纯色主题：天蓝色、科技蓝、科技紫、橙黄色、棕色、墨灰色、奶绿色
- 可选纸张质感效果
- 米白色背景，支持细微纸纹、轻微噪点纹理

## 开发者信息
- **作者**：Makur
- **QQ**：3397023886
- **GitHub**：https://github.com/makur6371
- **邮箱**：makur6371@gmail.com

## 开源协议
本项目采用MIT开源协议，详情请参阅LICENSE文件。

## 更新日志
### v1.0.0
- 初始版本发布
- 实现基本的想法记录功能
- 集成硅基流动AI服务
- 实现闪念网络视图
- 提供多种主题选择

## 贡献指南
欢迎提交Issue和Pull Request来帮助改进这个项目！

## 致谢
感谢硅基流动提供的AI服务支持！