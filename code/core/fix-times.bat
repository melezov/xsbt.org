@echo off

echo Fixing the file modification times ...
call "%~dp0\sbt.bat" --no-jrebel %* run
