CC = gcc
OPT = -O6 -funroll-loops
CFLAGS = -Wall -g $(OPT)

all: eaf eaf-test

eaf: eaf.o eaf_input.o eaf_main.o
	$(CC) -o eaf eaf.o eaf_input.o eaf_main.o

eaf-test: eaf_test.o eaf_input.o eaf_test_main.o
	$(CC) -o eaf-test eaf_test.o eaf_input.o eaf_test_main.o

eaf.o:	eaf.c eaf.h
eaf_input.o: eaf_input.c eaf.h
eaf_main.o: eaf_main.c eaf.h
eaf_test.o: eaf_test.c eaf.h
eaf_test_main.o: eaf_test_main.c eaf.h

eaf2-ptest: main-int-ptest.o io.o eaf_test.o
	$(CC) -o eaf2-ptest main-int-ptest.o io.o eaf_test.o

clean:
	rm -f eaf.o eaf_input.o eaf_main.o eaf_test_main.o eaf_test.o

distclean: clean
	rm -f eaf eaf-test *~

