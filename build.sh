#!/bin/sh

export PLAYPATH=/Applications/play-1.4.1
if [ "$1" != "" ]; then
	export PLAYPATH=$1
fi
export PLAYCMD=$PLAYPATH/play

echo ===========================================
echo "SERVER"
echo ===========================================

cd server
$PLAYCMD deps
if [ $? != 0 ]; then
    exit $?
fi

echo ...........................................
echo "SERVER : precompile"
echo ...........................................
$PLAYCMD precompile
if [ $? != 0 ]; then
    exit $?
fi

echo ...........................................
echo "SERVER : auto-test"
echo ...........................................
$PLAYCMD auto-test
if [ $? != 0 ]; then
    exit $?
fi

echo ...........................................
echo "SERVER : run tests"
echo ...........................................
./RunTests.sh $PLAYPATH
if [ $? != 0 ]; then
    exit $?
fi

npm install
bower install
if [ $? != 0 ]; then
    exit $?
fi

echo ...........................................
echo "SERVER : grunt (build et tests partie front)"
echo ...........................................
grunt
if [ $? != 0 ]; then
    exit $?
fi

cd ..

echo ===========================================
echo "CLIENT (partie config)"
echo ===========================================

cd client
npm install
if [ $? != 0 ]; then
    exit $?
fi

bower install
if [ $? != 0 ]; then
    exit $?
fi

echo ...........................................
echo "CLIENT : grunt"
echo ...........................................

grunt
if [ $? != 0 ]; then
    exit $?
fi

cd ..
