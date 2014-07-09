#!/usr/bin/perl -w

while (<>) {

  $termin = $_;
  $termin =~ s/.*;(.*):.*:.*;.*$/$1/;
  $locktime = $_;
  $locktime =~ s/.*;(.*):..:..$/$1/;
  
  if ($termin ne $locktime) {
    s/(.*);(.*);(.*);(\d{4}-\d{2}-\d{2} \d{2}:\d{2}).*;(.*)$/$1 $2 $3 - PROBLEM: $4 --> $5/;
    print;
  }
  
}