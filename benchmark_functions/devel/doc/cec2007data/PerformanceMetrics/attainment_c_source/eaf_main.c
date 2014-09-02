/*
 * (c) Carlos Fonseca, Universidade do Algarve
 *
 * Usage:
 *   eaf -l <n> -o <output file> <data file>
 *   computes the nth attainment surface for a set of runs stored in the data file
 *   and writes it to the output file
 *
 *   eaf -o <output file> <data file>
 *   computes all attainment surfaces for a set of runs stored in the data file
 *   and writes them to the output file
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "eaf.h"

int main(int argc, char *argv[])
{

    double *data = NULL;
    int* cumsizes = NULL;
    int nobj = 0, nruns = 0, *attlevel, nlevels;
    int i, c, k;
    char *attlevel_str = NULL;
    char *optarg = NULL;
    FILE *infile = stdin;
    FILE *coord = stdout;
    FILE *indic = NULL;

    /* Determine input and output files,
       and get list of attlevels in string format to decode later
       (we may need to read the data before we can determine nlevels) */
    for (i = 1; i < argc - 1 && argv[i][0] == '-'; i += 2) {
	c = argv[i][1];
	optarg = argv[i + 1];
	switch (c) {
	case 'l':
	    attlevel_str = optarg;
	    break;
	case 'o':
	    if (strcmp(optarg,"-")==0)
		coord = stdout;
	    else
		coord = fopen(optarg,"w");
	    break;
	case 'i':
	    if (strcmp(optarg,"-")==0)
		indic = stdout;
	    else
		indic = fopen(optarg,"w");
	    break;
	default:
	    ;
	}
	fprintf(stderr, "%c: %s\n", c, optarg);
    }
    
    for (k = i; k < argc; k++)
	if (strcmp(argv[k],"-")) {
	    infile = fopen(argv[k],"r");
	    if (infile)
		input_double(infile, &data, &nobj, &cumsizes, &nruns);
	    else {
		fprintf(stderr, "ERROR: Cannot open input file '%s'\n", argv[k]);
		exit(1);
	    }  
	    fclose(infile);
	} else
	    input_double(stdin, &data, &nobj, &cumsizes, &nruns);

    nlevels = attlevel_str ? 1 : nruns;
    attlevel = malloc(nlevels * sizeof(unsigned));
    if (attlevel_str)
	attlevel[0] = strtoul(attlevel_str, NULL, 0);
    else
	for(k = 0; k < nlevels; k++)
	    attlevel[k] = k+1;

    attsurf(data, nobj, cumsizes, nruns, attlevel, nlevels, coord, indic);
    
    if (indic)
      fclose(indic);
    fclose(coord);
    
    free(attlevel);
    free(data);
    free(cumsizes);
    return 0;
}
