set target=..\jar
set target1=..\data

call mvn clean
call mvn compile
call mvn package

mkdir %target%

mkdir %target%\res
mkdir %target%\data
mkdir %target%\logging
mkdir %target%\db

copy /y .\res\*.json %target%\res\
copy /y .\target\*.jar %target%\
copy /y .\Dockerfile %target%\
copy /y .\*.yml %target%\
copy /y .\*.sh %target%\

mkdir %target1%
copy /y %target1%\*.json %target%\data


pause