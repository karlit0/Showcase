#!/bin/bash

broj_rijeci=0
for file in `find ./ -type f -mtime -14`; do	
	echo "$file `wc -l $file | cut -d " " -f 1`"		
done
