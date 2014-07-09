#!/usr/bin/perl

$loaded = 0;

while (<>) {
  if (m/#.*/) { # skippaj komentare
    next;
  }
  
  chomp;
    
  if (!$loaded) {
    $factors = $_;  
    $loaded =1;
    while ($factors =~ m/;/) {
      $number = $factors;
      $number =~ s/([^;]*);.*/$1/;
      push @factors, $number;
      $factors =~ s/[^;]*;(.*)/$1/;
    }
    push @factors, $factors;
    next;
  }  
  
  $bodovi = $_;
  $bodovi =~ s/[^;]*;[^;]*;[^;]*;(.*)/$1/;

  $result = 0;
  $i = 0;
  while ($bodovi =~ m/;/) {
    $number = $bodovi;
    $number =~ s/([^;]*);.*/$1/;
    if ($number ne "-") {
      $result += $number * $factors[$i];
    }
    $i++;
    $bodovi =~ s/[^;]*;(.*)/$1/;
  }
  if ($bodovi ne "-") {
    $result += $bodovi * $factors[$i];
  }
  
  s/([^;]*;[^;]*;[^;]*).*/$result;$1/; # gurni rezultat prije ostalog infoa, na taj nacin ce numericka usporedba ignorirati sve nakon rezultata
  push @ppl, $_;
}

@ppl = sort {$b <=> $a} @ppl; # sortiraj numericki descending
print "Lista po rangu:\n";
print "-------------------\n";
$i = 1;
foreach (@ppl) {
  $result = $_;
  $result =~ s/([^;]*).*/$1/;
  s/([^;]*);([^;]*);([^;]*);([^;]*)/ $i. $3, $4 ($2)\t:/;
  print;
  printf "%.2f \n", $result;
  $i++;
}

