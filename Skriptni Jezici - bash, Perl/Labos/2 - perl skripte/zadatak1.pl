#!/usr/bin/perl -w

print "Znakovni niz: ";
$line = <STDIN>;
print "Broj ponavljanja: ";
$number = <STDIN>;
for ($i = 0; $i < $number; $i++) {
  print $line;
}

