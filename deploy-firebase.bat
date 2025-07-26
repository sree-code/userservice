@echo off
echo Building and deploying User Service to Firebase...

echo Building Maven project...
call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo Maven build successful!
    
    echo Installing Firebase dependencies...
    call npm install
    
    echo Deploying to Firebase...
    call firebase deploy
    
    echo Deployment completed!
) else (
    echo Maven build failed. Please check the errors above.
    exit /b 1
)
