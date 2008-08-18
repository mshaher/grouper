@echo off

rem if not in bin dir, but in grouper_home, then cd to bin
IF EXIST bin\encrypt.bat cd bin

IF NOT EXIST encrypt.bat echo Cant find file encrypt.bat.  Are you in the grouper_home dir or grouper_home\bin dir?
IF NOT EXIST encrypt.bat exit /b 1

set JAVA=java

set GROUPERENCRYPT=edu.internet2.middleware.grouper.util.rijndael.Encrypt

set GROUPER_HOME=..

set GROUPER_CONF=../conf

set MEM_START=64m

set MEM_MAX=256m

%JAVA% -Xms%MEM_START% -Xmx%MEM_MAX% -jar %GROUPER_HOME%/lib/invoker.jar -cpdir %GROUPER_CONF% -cpalljars %GROUPER_HOME%/lib -cpjar %GROUPER_HOME%/dist/lib/grouper.jar %GROUPERENCRYPT_JVMARGS% %GROUPERENCRYPT% %*
