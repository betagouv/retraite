#!/bin/sh
export PLAYPATH=/Applications/play-1.4.1
if [ "$1" != "" ]; then
	export PLAYPATH=$1
fi
echo $PLAYPATH
export CLASSPATH=.:$PLAYPATH/framework/lib/*:$PLAYPATH/framework/*:tmp/classes:lib/*:modules/pdf-0.9/lib/*:$PLAYPATH/modules/docviewer/lib/*
find test -name "*Test.java" | sed -e "s/test\///" -e "s/\.java//" -e "s/\//./g" | xargs java -cp $CLASSPATH org.junit.runner.JUnitCore
