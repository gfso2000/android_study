@ECHO OFF
SET "JAVA_HOME=C:\jdk1.8.0_151"
SET "JRE_HOME=C:\jdk1.8.0_151\jre"
SET "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME: %JAVA_HOME%
echo JRE_HOME: %JRE_HOME%
echo PATH: %PATH%
REM -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 
java -jar target/runshell-0.0.1-SNAPSHOT.jar C:/Users/i325639/AppData/Local/Android/Sdk/platform-tools/adb.exe
