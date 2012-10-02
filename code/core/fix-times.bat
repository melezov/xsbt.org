@echo off

echo Fixing the file modification times ...
call "%~dp0sbt.bat" --no-jrebel %* "run org.xsbt.versions.SBTVersions"
