/*
 * (c) Carlos Fonseca, Universidade do Algarve
 */

/* attsurf.h - prototypes for the attsurf functions */

int attsurf(double *data,	/* the objective vectors         */
	    int nobj,		/* the number of objectives      */
	    int *cumsize,	/* the cumulative sizes of the runs */
	    int nruns,		/* the number of runs            */
	    int *attlevel,	/* the desired attainment levels */
	    int nlevels,	/* the number of att levels      */
	    FILE *coord,	/* output file (coordinates)	 */
	    FILE *indic		/* output file (attainment indicators) */
);

int input_double(FILE *infile, double **datap, int *nvarsp, int **cumsizesp, int *nrunsp);
int input_int(FILE *infile, int **datap, int *nvarsp, int **cumsizesp, int *nrunsp);

int eaf_ks_stat(int *data, int nvars, int npoints);
int eaf2_ks_stat(int *data, int nvars, int npoints);

int *eaf_ks_tail(int *data, int nvars, int npoints, double alpha, int nperms);
int *eaf2_ks_tail(int *data, int nvars, int npoints, double alpha, int nperms);
