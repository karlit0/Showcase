#!/bin/bash

if [ "$#" -ne 2 ]
then
	echo "Usage $0 target_folder pattern"
	exit 1
fi

target_folder=$1
pattern=$2
echo "\$0 = $target_folder"
echo "\$1 = $pattern"
echo

broj_redaka=0
for file in `find $target_folder -name "$pattern"`
do
	broj_redaka=$(($broj_redaka + `wc -l $file | cut -d " " -f 1`))
done

echo $broj_redaka
