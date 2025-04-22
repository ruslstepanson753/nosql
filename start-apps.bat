@echo off
echo Starting publisher and discussion modules asynchronously...
echo.

start "Publisher Module" cmd /c "cd publisher && mvn spring-boot:run"
start "Discussion Module" cmd /c "cd discussion && mvn spring-boot:run"

echo Both applications started in separate windows.
echo Press Ctrl+C to exit this console.
pause