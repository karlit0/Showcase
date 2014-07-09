#!/bin/bash

for file in localhost_access_log.20??-02-??.txt
do
	date=`echo "$file" | sed -r 's/.*([0-9]{4})-02-([0-9]{2}).txt/\2-02-\1/'`
	echo "datum: $date"
	echo "----------------------------------------------"
	cut -d "\"" -f 2 "$file" | sed 's/^/: /'| sort | uniq -c | tr -s ' ' | sort -nr
	echo
done
