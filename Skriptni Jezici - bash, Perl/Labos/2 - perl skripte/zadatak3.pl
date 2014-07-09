#!/usr/bin/perl -w

$filename = "";
$finished_first = 0;
for ($i = 0; $i < 24; $i++) {
  $hours[$i] = 0;
}

while (<>) {
  if ($ARGV ne $filename) {
    if ($filename ne "") {
      $finished_first = 1;
    }
    $filename = $ARGV;
    $date = $filename;
    $date =~ s/.*(\d{4}-\d{2}-\d{2}).*/$1/; # pretpostavlja ime datoteka formata localhost_access_log.xxxx-xx-xx

    if ($finished_first) { # skippaj rezultate prvog filea (jer ih nemas dostupne jos)
      for ($i = 0; $i < 24; $i++) {
        if ($i < 10) {
          print "  0$i : $hours[$i]\n";
        } else {
          print "  $i : $hours[$i]\n";
        }
      }
    }
    
    print "Datum: $date\n";
    print " sat : broj pristupa \n";
    
    for ($i = 0; $i < 24; $i++) {
      $hours[$i] = 0;
    }
  }

  for ($i = 0; $i < 24; $i++) {
    if ($i < 10) {
      if (m/\d{4}:0$i:\d{2}:\d{2}/) {
        $hours[$i]++;  
      }
    } else {
      if (m/\d{4}:$i:\d{2}:\d{2}/) {
        $hours[$i]++;
      }
    }
  }
  

}

#ispisi rezultate zadnjeg filea
for ($i = 0; $i < 24; $i++) {
  if ($i < 10) {
    print "  0$i : $hours[$i]\n";
  } else {
  print "  $i : $hours[$i]\n";
  }
}