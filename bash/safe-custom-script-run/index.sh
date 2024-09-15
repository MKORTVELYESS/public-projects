#!/bin/bash

execute_file="./app-v2.2_app-v2.3.txt"
allowlist_file="./whitelist.txt"

echo "This is the meta build system"

echo "Start execute"
 
echo "Start verify"

#STEP 1 - custom script
	./custom-script.sh $allowlist_file $execute_file
	exit_code=$?
	echo "The exit code of the verify script was: $exit_code"

#STEPS...
	exit $exit_code


