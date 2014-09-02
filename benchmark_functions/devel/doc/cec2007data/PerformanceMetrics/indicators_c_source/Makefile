CC=gcc
CPP=g++
CFLAGS=-c -Wall -pedantic
LDFLAGS=-lm
SOURCES=dominance-rank.cc eps_ind.c hyp_ind.c r_ind.cc
OBJECTS=dominance-rank.o eps_ind.o hyp_ind.o r_ind.o

all: dominance-rank eps_ind hyp_ind r_ind

dominance-rank: dominance-rank.o
	$(CPP) $(LDFLAGS) -o dominance-rank dominance-rank.o

dominance-rank.o: dominance-rank.cc

eps_ind: eps_ind.o
	$(CC) $(LDFLAGS) -o eps_ind eps_ind.o

eps_ind.o: eps_ind.c

hyp_ind: hyp_ind.o
	$(CC) $(LDFLAGS) -o hyp_ind hyp_ind.o

hyp_ind.o: hyp_ind.c

r_ind: r_ind.o
	$(CPP) $(LDFLAGS) -o r_ind r_ind.o

r_ind.o: r_ind.cc

.cc.o:
	$(CPP) $(CFLAGS) $< -o $@
.c.o:
	$(CC) $(CFLAGS) $< -o $@

clean:
	 rm -f *~ *.o


