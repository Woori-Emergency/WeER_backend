#!/bin/sh

#Setting Versions
VERSION='1.0.0'

export JAVA_HOME="C:/Users/2-20/.jdks/corretto-21.0.4"   # Java 17 경로로 변경하세요
export PATH=$JAVA_HOME/bin:$PATH

cd ..
./gradlew clean build -x test

ROOT_PATH=$(pwd)
echo "$ROOT_PATH"

echo 'api docker image build... Start'
cd "$ROOT_PATH"/docker/ && docker build -t weer_backend:$VERSION .
echo 'api docker image build... Finish'