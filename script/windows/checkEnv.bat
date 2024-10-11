@echo off
chcp 65001 > NUL
:: 获取java -version命令的输出
java -version > temp.txt 2>&1
:: 获取java -version命令的输出
for /f "tokens=3 delims= " %%i in ('findstr /i "version" temp.txt') do set CURR_JAVA_VERSION=%%i
:: 最低JAVA版本
set MIN_JAVA_HOME=17
:: 清除临时文件
del temp.txt
echo %CURR_JAVA_VERSION% | findstr %MIN_JAVA_HOME% >nul
if %errorlevel% neq 0 (
	echo "当前Java版本不支持，版本要求%MIN_JAVA_HOME%+"
	cmd /k
)
