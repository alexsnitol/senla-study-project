@echo off
echo Drop database if exist
"%programfiles%\PostgreSQL\10\bin\psql.exe" -U postgres -c "DROP DATABASE IF EXISTS db_autoservice"
echo.
echo Create database
"%programfiles%\PostgreSQL\10\bin\psql.exe" -U postgres -c "CREATE DATABASE db_autoservice"
echo.
echo Insert data in database
"%programfiles%\PostgreSQL\10\bin\psql.exe" -U postgres -d "db_autoservice" -f "%cd%\setup_db_autoservice.sql"
echo.
echo Database setup is complete
pause