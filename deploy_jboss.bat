mkdir server\www
del /S /Q server\www

xcopy /S /Y /Q client\www server\www


cmd /c play war server -o retraite --zip --exclude .sass-cache:.settings:eclipse:javawatch:node_modules:report:test:testjs:tmp



copy retraite.war C:\usine-dev\workdir\jboss-local\standalone\deployments