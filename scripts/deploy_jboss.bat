set JAVA_HOME=C:\usine-dev\programs\jdk\jdk-1.7.0_79

cd client
cmd /c grunt build
cd ..

mkdir server\www
del /S /Q server\www
del retraite.war

xcopy /S /Y /Q client\www server\www


cmd /c play war server -o retraite --zip --exclude .sass-cache:.settings:eclipse:javawatch:node_modules:report:test:testjs:tmp --%%jbosslocal



copy retraite.war C:\usine-dev\workdir\jboss-local\standalone\deployments