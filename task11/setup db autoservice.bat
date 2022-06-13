@REM "c:\Program Files\PostgreSQL\10\bin\createdb.exe" -U postgres db_autoservice
@echo off
echo Drop database if exist
"c:\Program Files\PostgreSQL\10\bin\psql.exe" -U postgres -c "DROP DATABASE IF EXISTS db_autoservice"
echo Create database
"c:\Program Files\PostgreSQL\10\bin\psql.exe" -U postgres -c "CREATE DATABASE db_autoservice"
echo Insert data in database
"c:\Program Files\PostgreSQL\10\bin\psql.exe" -U postgres -d "db_autoservice" -f "D:\project\gitlab\alexander_slotin\task11\setup_db_autoservice.sql"
echo Set up database is finish
pause