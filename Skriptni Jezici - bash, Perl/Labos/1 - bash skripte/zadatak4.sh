#!/bin/bash

if [ "$#" -ne 2 ]
then
	echo "Usage: $0 izvorisni_direktorij odredisni_direktorij"
	exit 1
fi

source_folder=$1
dest_folder=$2

for file in $source_folder/*.jpg
do
	if [ ! -e "$file" ]
	then
		echo "Source folder doesn't exist or it doesn't contain any pictures"
		exit 2
	fi
done

if [ ! -d "$dest_folder" ]
then
	mkdir $dest_folder
fi

for file in $source_folder/*.jpg
do				
	date=`stat $file | tail -n 3 | head -n 1 | cut -d " " -f 2 | sed 's/...$//'`

	if [ ! -d "$dest_folder"/"$date" ]
	then
		mkdir $dest_folder/$date
	fi
				
	mv $file $dest_folder/$date/
done

