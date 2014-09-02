/*
 * (c) Carlos Fonseca, Universidade do Algarve
 */

#include <stdlib.h>
#include <stdio.h>
#include "eaf.h"

#define PAGE_SIZE 4096
#define DOUBLE_DATA_INC (PAGE_SIZE/sizeof(double))
#define INT_DATA_INC (PAGE_SIZE/sizeof(int))
#define SIZES_INC (PAGE_SIZE/sizeof(int))


/*
 * Read an array of double from file. This function may be called repeatedly to
 * add data to an existing data set.
 */

int input_double(FILE *infile, double **datap, int *nvarsp, int **cumsizesp, int *nrunsp)
{
    double x;

    int c;			/* return value for fscanf */
    int i;			/* the current element of (*datap) */
    int n;			/* the column being read */
    int l;			/* the current data set */
    int nvars = (*nvarsp);	/* the number of columns */

    char b[2];

    int datasize;
    int sizessize;

    l = (*nrunsp);
    i = (l==0) ? 0 : nvars * (*cumsizesp)[l-1];

    sizessize = (l==0) ? 0 : ((l-1) / SIZES_INC + 1) * SIZES_INC;
    *cumsizesp = realloc(*cumsizesp, sizessize * sizeof(int));
    datasize = (i==0) ? 0 : ((i-1) / DOUBLE_DATA_INC + 1) * DOUBLE_DATA_INC;
    *datap = realloc(*datap, datasize * sizeof(double));

    c = 0;
    while (c != EOF) {
	if (l == sizessize)
	    *cumsizesp = realloc(*cumsizesp, (sizessize += SIZES_INC) * sizeof(int));
	(*cumsizesp)[l] = (l==0) ? 0 : (*cumsizesp)[l-1];	/* beginning of data set */
	while (c == 0) {
	    n = 0;		/* beginning of row */
	    while (c == 0) {
		/* read number */
		if (fscanf(infile, "%lf", &x) == 1) {
		    n++;	/* new column */
		    if (i == datasize)
			*datap = realloc(*datap, (datasize += DOUBLE_DATA_INC) * sizeof(double));
		    (*datap)[i++] = x;
#if DEBUG
		    fprintf(stderr, "Set %d, row %d, column %d, x = %d\n", l, (*cumsizesp)[l], n, x);
#endif
		}
		else {
		    fprintf(stderr, "Error: Could not convert string (aborting)\n");
		    exit(1);
		}
		fscanf(infile, "%*[ \t]");
		c = fscanf(infile, "%1[\r]",b);
		c |= fscanf(infile, "%1[\n]",b);
	    }
	    if (!nvars)
		nvars = n;
	    else if (n != nvars) {
		fprintf(stderr, "Error: Found rows with different numbers of columns (aborting)\n");
		exit(1);
	    }
	    (*cumsizesp)[l]++;
	    fscanf(infile, "%*[ \t]");
	    c = fscanf(infile, "%1[\r]",b);
	    c |= fscanf(infile, "%1[\n]",b);
	}
	l++;			/* new data set */
#if 0
	fprintf(stderr, "Set %d, read %d rows\n", l, (*cumsizesp)[l-1]);
#endif
	/* skip over any trailing whitespace */
	fscanf(infile,"%*[ \t\r\n]");
	/* check for EOF */
	c = fscanf(infile,"%1[ \t\r\n]",b);
    }
    *cumsizesp = realloc(*cumsizesp, l * sizeof(int));
    *datap = realloc(*datap, i * sizeof(double));
    *nvarsp = nvars;
    *nrunsp = l;

    return 0;
}


/*
 * Read an array of int from file. This function may be called repeatedly to
 * add data to an existing data set.
 */

int input_int(FILE *infile, int **datap, int *nvarsp, int **cumsizesp, int *nrunsp)
{
    int x;

    int c;			/* return value for fscanf */
    int i;			/* the current element of (*datap) */
    int n;			/* the column being read */
    int l;			/* the current data set */
    int nvars = (*nvarsp);	/* the number of columns */

    char b[2];

    int datasize;
    int sizessize;

    l = (*nrunsp);
    i = (l==0) ? 0 : nvars * (*cumsizesp)[l-1];

    sizessize = (l==0) ? 0 : ((l-1) / SIZES_INC + 1) * SIZES_INC;
    *cumsizesp = realloc(*cumsizesp, sizessize * sizeof(int));
    datasize = (i==0) ? 0 : ((i-1) / INT_DATA_INC + 1) * INT_DATA_INC;
    *datap = realloc(*datap, datasize * sizeof(int));

    c = 0;
    while (c != EOF) {
	if (l == sizessize)
	    *cumsizesp = realloc(*cumsizesp, (sizessize += SIZES_INC) * sizeof(int));
	(*cumsizesp)[l] = (l==0) ? 0 : (*cumsizesp)[l-1];	/* beginning of data set */
	while (c == 0) {
	    n = 0;		/* beginning of row */
	    while (c == 0) {
		/* read number */
		if (fscanf(infile, "%d", &x) == 1) {
		    n++;	/* new column */
		    if (i == datasize)
			*datap = realloc(*datap, (datasize += INT_DATA_INC) * sizeof(int));
		    (*datap)[i++] = x;
#if DEBUG
		    fprintf(stderr, "Set %d, row %d, column %d, x = %d\n", l, (*cumsizesp)[l], n, x);
#endif
		}
		else {
		    fprintf(stderr, "Error: Could not convert string (aborting)\n");
		    exit(1);
		}
		fscanf(infile, "%*[ \t]");
		c = fscanf(infile, "%1[\r]",b);
		c |= fscanf(infile, "%1[\n]",b);
	    }
	    if (!nvars)
		nvars = n;
	    else if (n != nvars) {
		fprintf(stderr, "Error: Found rows with different numbers of columns (aborting)\n");
		exit(1);
	    }
	    (*cumsizesp)[l]++;
	    fscanf(infile, "%*[ \t]");
	    c = fscanf(infile, "%1[\r]",b);
	    c |= fscanf(infile, "%1[\n]",b);
	}
	l++;			/* new data set */
#if 0
	fprintf(stderr, "Set %d, read %d rows\n", l, (*cumsizesp)[l-1]);
#endif
	/* skip over any trailing whitespace */
	fscanf(infile,"%*[ \t\r\n]");
	/* check for EOF */
	c = fscanf(infile,"%1[ \t\r\n]",b);
    }
    *cumsizesp = realloc(*cumsizesp, l * sizeof(int));
    *datap = realloc(*datap, i * sizeof(int));
    *nvarsp = nvars;
    *nrunsp = l;

    return 0;
}


