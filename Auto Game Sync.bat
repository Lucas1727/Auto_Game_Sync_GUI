@echo off
title Auto Game Sync

echo Starting to Sync..
echo.

::copy libraries\jansi-1.14.jar "C:\Program Files\Java\jre1.8.0_121\lib\ext"

cd "out\artifacts\AutoGameSync\"

java -jar AutoGameSync.jar

::del "C:\Program Files\Java\jre1.8.0_121\lib\ext\jansi-1.14.jar"

pause 