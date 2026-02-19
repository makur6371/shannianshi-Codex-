@echo off
echo 闪念式APK生成工具
echo ==================
echo.

echo 由于编译APK需要Java和Android SDK环境，
echo 请按照以下步骤操作：
echo.

echo 步骤1: 安装必要工具
echo ---------
echo 1. 安装Java JDK 11或更高版本:
echo    https://jdk.java.net/11/
echo.
echo 2. 安装Android Studio:
echo    https://developer.android.com/studio
echo.

echo 步骤2: 设置环境变量
echo ---------
echo 在系统环境变量中添加:
echo JAVA_HOME = C:\Program Files\Java\jdk-11
echo Path = %%JAVA_HOME%%\bin;%%PATH%%
echo.
echo ANDROID_SDK_ROOT = C:\Android\Sdk
echo Path = %%ANDROID_SDK_ROOT%%\platform-tools;%%PATH%%
echo.

echo 步骤3: 编译APK
echo ---------
echo 打开命令提示符，进入项目目录:
echo cd E:\codex\闪念式
echo.
echo 然后运行:
echo gradlew clean assembleDebug
echo.
echo 生成的APK将在 app/build/outputs/apk/debug/ 目录下
echo.

echo 正在创建项目结构验证...
.

REM 检查关键文件
echo [检查项目文件]...

if exist "app\build.gradle" (
    echo [✓] app\build.gradle 存在
) else (
    echo [✗] 缺少 app\build.gradle
)

if exist "app\src\main\AndroidManifest.xml" (
    echo [✓] AndroidManifest.xml 存在
) else (
    echo [✗] 缺少 AndroidManifest.xml
)

if exist "gradlew" (
    echo [✓] gradlew 存在
) else (
    echo [✗] 缺少 gradlew
)

if exist "settings.gradle" (
    echo [✓] settings.gradle 存在
) else (
    echo [✗] 缺少 settings.gradle
)

echo.
echo 如果所有检查都通过，您可以使用以下命令编译：
echo.
echo gradlew clean assembleDebug
echo.
echo 或者直接运行:
echo compile-with-studio.bat
echo.

pause