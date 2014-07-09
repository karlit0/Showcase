#!/usr/bin/perl -w

@list = <STDIN>;
$result = 0;

foreach $number (@list) {
  $result += $number;
}

if (@list > 0) {
  $result = $result / @list;
}

print "Aritmeticka sredina: ", $result;
