
set specpath=%cd%\build
set output=%cd%\build\bin
set distpath=%cd%\build\dist

set script=%cd%\app.py
set icon=%cd%\icons\hansagt128.ico

:: --onedir or--onefile

pyinstaller --noconfirm --onedir --windowed --icon %icon% ^
    --workpath %output% --distpath %distpath% --specpath=%specpath% %script%

copy /y .\config-prod.yaml %distpath%\app
xcopy /y .\assets %distpath%\app\asserts\ /e