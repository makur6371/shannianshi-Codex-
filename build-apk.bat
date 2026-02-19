@echo off
echo 正在编译闪念式APK...
echo.

REM 检查是否安装了Android SDK
if not exist "C:\Android\Sdk\cmdline-tools\latest\bin\sdkmanager.bat" (
    echo.
    echo [错误] 未找到Android SDK
    echo 请先安装Android Studio：
    echo 1. 访问 https://developer.android.com/studio
    echo 2. 下载并安装Android Studio
    echo 3. 安装时选择"Android SDK Platform"和"Android SDK Build-Tools"
    echo 4. 确保SDK安装在 C:\Android\Sdk 或默认位置
    echo.
    pause
    exit /b 1
)

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo [错误] 未找到Java
    echo 请先安装Java Development Kit (JDK) 11或更高版本：
    echo 访问 https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

REM 设置Android SDK环境变量
set ANDROID_SDK_ROOT=C:\Android\Sdk
set PATH=%PATH%;%ANDROID_SDK_ROOT%\tools;%ANDROID_SDK_ROOT%\platform-tools;%ANDROID_SDK_ROOT%\cmdline-tools\latest\bin

echo 检查Android SDK组件...
echo.

REM 检查必要的SDK组件
if not exist "%ANDROID_SDK_ROOT%\platform-tools\adb.exe" (
    echo [警告] 缺少platform-tools，正在安装...
    call "%ANDROID_SDK_ROOT%\cmdline-tools\latest\bin\sdkmanager.bat" "platform-tools"
    echo.
)

if not exist "%ANDROID_SDK_ROOT%\build-tools\34.0.0\aapt.exe" (
    echo [警告] 缺少build-tools，正在安装...
    call "%ANDROID_SDK_ROOT%\cmdline-tools\latest\bin\sdkmanager.bat" "build-tools;34.0.0"
    echo.
)

echo 开始编译APK...
echo.

REM 清理项目
call gradlew clean

REM 构建APK
call gradlew assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo [成功] APK编译完成！
    echo APK文件位置：app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 您可以使用以下命令安装APK：
    echo adb install app\build\outputs\apk\debug\app-debug.apk
    echo.
) else (
    echo.
    echo [错误] APK编译失败！
    echo 请检查错误信息并重试。
    echo.
    pause
    exit /b 1
)

pause