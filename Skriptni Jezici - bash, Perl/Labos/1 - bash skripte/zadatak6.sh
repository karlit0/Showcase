#!/bin/bash

if [ "$#" -lt 2 ]
then
	echo "Usage: $0 file... dest_folder"
	exit 1
fi

eval dest_folder=\${$#}

if [ ! -d "$dest_folder" ]
then
	mkdir $dest_folder
	echo "Kreirano je kazalo $dest_folder"
fi

counter=0
for file in $@
do
	if [ -f "$file" -a -r "$file" ]
	then
		cp $file $dest_folder/
		counter=$((counter+1))
	fi
done

echo "Number of sucessful copies into $dest_folder: $counter"