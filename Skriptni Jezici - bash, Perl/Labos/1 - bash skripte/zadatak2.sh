#!/bin/bash

grep -E -i "banana|jabuka|jagoda|dinja|lubenica" namirnice.txt
echo

grep -E -iv "banana|jabuka|jagoda|dinja|lubenica" namirnice.txt > ne-voce.txt
echo

grep -rn -E "[[:upper:]][[:digit:]]{6}" ~/projekti/*
echo

find ./ -mtime +6 -mtime -14 -ls
echo

for i in {1..15}; do echo $i; done 