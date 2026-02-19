@echo off
echo 正在使用Android Studio编译闪念式APK...
echo.

REM 检查Android Studio是否安装
set STUDIO_PATH="C:\Program Files\Android\Android Studio\bin\studio64.exe"
if not exist %STUDIO_PATH% (
    set STUDIO_PATH="C:\Program Files\Android Studio\bin\studio64.exe"
)
if not exist %STUDIO_PATH% (
    echo.
    echo [错误] 未找到Android Studio
    echo 请先安装Android Studio：
    echo 1. 访问 https://developer.android.com/studio
    echo 2. 下载并安装Android Studio
    echo 3. 确保包含了Android SDK
    echo.
    pause
    exit /b 1
)

REM 设置Java环境（Android Studio自带）
set ANDROID_STUDIO_JAVA="C:\Program Files\Android Android Studio\jbr"
if not exist %ANDROID_STUDIO_JAVA% (
    set ANDROID_STUDIO_JAVA="C:\Program Files\Android Studio\jbr"
)
if exist %ANDROID_STUDIO_JAVA% (
    set JAVA_HOME=%ANDROID_STUDIO_JAVA%
    set PATH=%ANDROID_STUDIO_JAVA%\bin;%PATH%
)

echo 使用Android Studio自带的环境编译...

REM 检查Java是否可用
java -version
if %errorlevel% neq 0 (
    echo.
    echo [错误] Java不可用
    echo 请确保Android Studio安装正确
    echo.
    pause
    exit /b 1
)

echo.
echo 开始编译项目...
echo.

REM 方法1：使用Android Studio的命令行工具
echo 方法1：使用Android Studio命令行工具...

REM 检查gradlew是否存在
if not exist "gradlew" (
    echo [错误] 找不到gradlew脚本
    echo 请确保项目包含gradlew文件
    pause
    exit /b 1
)

REM 尝试使用gradlew编译
echo 正在使用gradlew编译...
call gradlew clean assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo [成功] APK编译完成！
    echo APK文件位置：app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 可以使用以下命令安装：
    echo adb install app\build\outputs\apk\debug\app-debug.apk
    echo.
    pause
    exit /b 0
) else (
    echo.
    echo [警告] gradlew编译失败，尝试其他方法...
    echo.
)

REM 方法2：生成一个简单的APK（如果Android Studio方法失败）
echo 正在创建APK文件...
mkdir app\build\outputs\apk\debug 2>nul

echo [调试信息] 尝试创建APK文件...

REM 创建一个基本的APK文件作为占位符
echo @echo off > create-apk.bat
echo echo 正在创建APK文件... >> create-apk.bat
echo echo 由于环境配置问题，创建一个演示APK文件 >> create-apk.bat
echo echo 实际使用请通过Android Studio编译 >> create-apk.bat
echo. >> create-apk.bat

REM 复制或创建APK
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo APK文件已存在：app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [提示] APK文件需要通过Android Studio编译生成
    echo 请手动编译项目：
    echo 1. 使用Android Studio打开此项目
    echo 2. 等待项目同步完成
    echo 3. 点击Build > Build Bundle(s) / APK(s) > Build APK(s)
    echo 4. APK将在 app/build/outputs/apk/debug/ 目录下生成
    echo.
)

echo 编译过程可能需要几分钟，请耐心等待...
pause