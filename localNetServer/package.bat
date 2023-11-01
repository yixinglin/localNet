call mvn clean
call mvn compile
call mvn package

set target=..\jar
mkdir %target%
mkdir %target%\res 
copy /y .\res\*.json %target%\res\
copy /y .\target\*.jar %target%\
pause