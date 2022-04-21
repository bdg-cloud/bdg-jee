#!/usr/bin/ruby

a = ARGV[0].gsub(/\n+\s*/m, ' ')
puts a[a.index('[')+1..a.index(']')-1].gsub(' ','')

