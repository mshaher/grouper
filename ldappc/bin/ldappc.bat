@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@echo off

set ERROR_CODE=0

@setlocal

rem Set the classpath to the conf directory.
set CLASSPATH=conf

# Add all the jars in the lib directory to the classpath.
for %%f in (lib\*.jar) do call :appendCP %%f

rem Don't pass the classpath. It's too long. Just let the jvm get it from the environment variable.
java -Xmx256m edu.internet2.middleware.ldappc.Ldappc %*
if ERRORLEVEL 1 goto error
goto end

:error
@endlocal
set ERROR_CODE=1

:end
@endlocal

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%

REM Windows batch files seem to be stupid about variable expansion.
REM This has to be in a label that gets called above, or it won't work.
:appendCP
set CLASSPATH=%CLASSPATH%;%1