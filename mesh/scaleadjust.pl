#!/usr/bin/perl -w

open(IN, $ARGV[0]);

while (<IN>) {
	chomp;
	my @r = split(",");
	$r[0] =~ s/f$//g;
	$r[1] =~ s/f$//g;
	$r[2] =~ s/f$//g;
	
	$r[0] /= 5;
	$r[1] /= 5;
	$r[2] /= 5;
	
	$r[0] = sprintf("%.5ff", $r[0]);
	$r[1] = sprintf("%.5ff", $r[1]);
	$r[2] = sprintf("%.5ff", $r[2]);
	print join(", ", @r).",\n";;
}

close(IN);