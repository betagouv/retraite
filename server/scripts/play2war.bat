set JAVA_HOME=C:\usine-dev\programs\jdk\jdk-1.7.0_79

cd ..

rmdir /Q /S retraite

cmd /c play war server -o retraite --exclude .sass-cache:.settings:eclipse:javawatch:node_modules:report:test:testjs:tmp:target:sql-imports:src:META-INF --%%jbosslocal

xcopy /S /Y /Q /E /I server\META-INF retraite\META-INF