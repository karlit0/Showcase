#!/usr/bin/perl -w

use open ':locale';
use locale;

$number = pop(@ARGV);
print "$number\n";

while (<>) {
  chomp;  
  print;
  print "\n";
  $line = $_;
  while ($line =~ m/\b\w{$number}/) {
    $word = $line;
    $word =~ s/.*(\b\w{$number}).*/$1/; #dohvati zadnju rijec
    $word = "\L$word\E"; #pretvori u male znakove

    if (!exists $words{$word}) {
      $words{$word} = 1;    
    } else {
      $words{$word}++;
    }

    $line =~ s/(.*)\b\w{$number}\w*/$1/; #obrisi zadnju rijec (i sve iza nje)
  }
}

foreach $key (sort keys %words) {
  print "$key : $words{$key}\n";
}