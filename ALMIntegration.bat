set projectlocation=%1
cd %projectlocation%
set classpath=%projectlocation%\bin\;%projectlocation%\libs\*
java itqs.Driver %2
pause
