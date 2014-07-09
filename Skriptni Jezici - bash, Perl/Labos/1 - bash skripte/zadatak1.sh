#!/bin/bash

proba="Ovo je proba"
echo $proba
echo

lista_datoteka=*.*
echo $lista_datoteka
echo

proba3="$proba. $proba. $proba. "
echo $proba3
echo

a=4
b=3
c=7
d=$((($a+4)*$b%$c))
echo "($a+4)*$b%$c=$d"
echo

broj_rijeci=`wc -w *.txt | tail -n 1 | cut -d " " -f 2`
echo $broj_rijeci
echo

ls ~
echo

cut -d ":" -f 1,6,7 /etc/passwd
echo

ps | tr -s " " | cut -d " " -f 2,7,9
