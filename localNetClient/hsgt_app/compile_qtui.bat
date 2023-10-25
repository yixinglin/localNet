:: pyuic5 logindialog.ui -o ui_logindialog.py

set DIR=.\components
pyuic5 -o %DIR%\ui_logindialog.py %DIR%\logindialog.ui

set DIR=.\views
pyuic5 -o %DIR%\ui_mainwindow.py %DIR%\mainwindow.ui

set DIR=.\views\pricing
pyuic5 -o %DIR%\ui_mainpricingboard.py %DIR%\mainpricingboard.ui
pyuic5 -o %DIR%\ui_pricingboard.py %DIR%\pricingboard.ui

set DIR=.\views\pricing\components
pyuic5 -o %DIR%\ui_competitorstat.py %DIR%\competitorstat.ui
pyuic5 -o %DIR%\ui_configurepricing.py %DIR%\configurepricing.ui
pyuic5 -o %DIR%\ui_edititemdialog.py %DIR%\edititemdialog.ui
pyuic5 -o %DIR%\ui_newofferdialog.py %DIR%\newofferdialog.ui



pause