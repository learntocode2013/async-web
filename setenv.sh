#!/bin/env bash
echo "Setting Java home to 1.8 level before compilation ..."
echo
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home
echo
echo "Setting Maven home to maven 3.3.9 in current workspace ..."
echo
export M2_HOME=/Data/Rest-Projects/apache-maven-3.3.9
export M2=$M2_HOME/bin
export PATH=$M2:$PATH

echo
echo "You are all set to rock and roll"

