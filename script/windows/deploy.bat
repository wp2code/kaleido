@echo off
chcp 65001 > NUL
call build.bat
echo "Deploy Kaliedo..."
set DEFAULT_PATH="D:\\wp2code\\github\\kaleido-client\\server\\app"
set /p APP_SERVER_PATH="输入发布的的路径（默认：%DEFAULT_PATH%）："
if "%APP_SERVER_PATH%"=="" set APP_SERVER_PATH=%DEFAULT_PATH%
echo "发布的路径是：%APP_SERVER_PATH%"
if exist "%APP_SERVER_PATH%\lib" (
	RD /Q /S %APP_SERVER_PATH%\lib
) else (
	mkdir %APP_SERVER_PATH%\lib
)
if exist "%APP_SERVER_PATH%\kaleido-server-start.jar" (
    DEL /Q /S %APP_SERVER_PATH%\kaleido-server-start.jar
)
cd ../../
cd kaleido-start\target
xcopy lib %APP_SERVER_PATH%\lib  /s /e /i
copy kaleido-server-start.jar %APP_SERVER_PATH%
cd ..
cmd /k
