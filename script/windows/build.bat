@echo off
chcp 65001 > nul
setlocal EnableDelayedExpansion
call :InitColorText
call checkEnv.bat
call :ColorText 09 "...Build Kaliedo"
echo/ 
:: echo Current directory is: %cd%
cd ../..
:: clean package 
call mvn clean package -Dmaven.test.skip=true -Pprod
call :ColorText 02 "Build Kaliedo SUCCESS"
echo/ 


:InitColorText
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do (
  set "DEL=%%a"
)
goto :eof

:ColorText
echo off
setlocal EnableDelayedExpansion
<nul set /p ".=%DEL%" > "%~2"
findstr /V /A:%1 /R "^$" "%~2" nul
del "%~2" > nul 2>&1
goto :eof





