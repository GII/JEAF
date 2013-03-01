#!/bin/sh

for f in `find . -name "*.java"`
do
	cp $f temp;
	cp copyright.txt $f;
	cat temp >> $f;
done

rm -f temp;
