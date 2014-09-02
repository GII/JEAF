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

static int compare_x_asc(const void *p1, const void *p2)
{
	double x1=**(double **)p1;
	double x2=**(double **)p2;
	
	if (x1 != x2)
		return (x1 < x2) ? -1 : 1;
	else
		return 0;
}

static int compare_y_desc(const void *p1, const void *p2)
{
	double y1 = *(*(double **)p1+1);
	double y2 = *(*(double **)p2+1);
	
	if (y1 != y2)
		return (y1 > y2) ? -1 : 1;
	else
		return 0;
}

/* 
 * attsurf - Compute attainment surfaces from points in objective space,
 *	using dimension sweeping
 *
 * Input arguments:
 *	data -	a pointer to the data matrix, stored as a linear array of double
 *		in row major order
 *	nobj -	the number of objectives (must be 2 in this implementation)
 *	cumsize - an array containing the cumulative number of rows in each non-dominated
 * 		front (must be non-decreasing)
 *	nruns -	the number of independent non-dominated fronts
 *	attlevel - an array containing the attainment levels to compute.
 *	nlevel - number of attainment levels to compute
 * 
 */

int attsurf(double *data, int nobj, int *cumsize, int nruns, int *attlevel, int nlevels, FILE *coord, FILE *indic)
{
	double **ix, **iy;		/* used to access the data sorted according to x or y */
	int gx, gy;			/* running indices for ix and iy */
	int ntotal		;	/* total number of points in data */
	int run, *runtab, level, l;	
	int i, j;			/* iteration counters */
	int *attained, nattained, *save_attained, save_nattained;
	
	if (nobj != 2) {
		fprintf(stderr, "ERROR: This implementation only supports two dimensions\n");
		return -1;
	}

	for (i = 1; i < nruns; i++)
		if (cumsize[i] <= cumsize[i-1]) {
			fprintf(stderr, "ERROR: the number of points in a front must be positive.\n");
			return -1;
		}
	ntotal = cumsize[nruns-1];

	/* Access to the data is made via two arrays of pointers: ix, iy
	 * These are sorted, to allow for dimension sweeping */
	
	ix = malloc(ntotal * sizeof(double*));
	iy = malloc(ntotal * sizeof(double*));

	for (i = 0; i < ntotal ; i++)
		ix[i] = iy[i] = data + 2*i;

#ifdef DEBUG
	fprintf (stderr, "Original data:\n");
	for (i = 0; i < ntotal ; i++)
		fprintf(stderr, "%6d: % .16e % .16e\n", i, ix[i][0], ix[i][1]);
#endif

	qsort(ix, ntotal, sizeof *ix, &compare_x_asc);
	qsort(iy, ntotal, sizeof *iy, &compare_y_desc);

#ifdef DEBUG
	fprintf (stderr, "Sorted data (x):\n");
	for (i = 0; i < ntotal ; i++)
		fprintf(stderr, "%6d: % .16e % .16e\n", i, ix[i][0], ix[i][1]);
	fprintf (stderr, "Sorted data (y):\n");
	for (i = 0; i < ntotal ; i++)
		fprintf(stderr, "%6d: % .16e % .16e\n", i, iy[i][0], iy[i][1]);
#endif

	/* Setup a lookup table to go from a point to the front to which it belongs */

	runtab = malloc(ntotal * sizeof(int));

	for (i = 0, j = 0; i < ntotal; i++) {
		if (i == cumsize[j])
			j++;
		runtab[i] = j;
	}

	/*
	 * Setup tables to keep attainment statistics
	 * save_attained is needed to cope with repeated values on the same axis
	 */

	attained = malloc(nruns * sizeof(int));
	save_attained = malloc(nruns * sizeof(int));
		
	for (l = 0; l < nlevels; l++) {
		level = attlevel[l];
		gx = 0;
		gy = 0;
		nattained = 0;
		for (i = 0; i < nruns; attained[i++] = 0);

		/* Start at upper-left corner */
		run = runtab[(ix[gx]-data)/nobj];
		attained[run]++;
		nattained++;

		do {
			/* Move right until desired attainment level is reached */
			while (gx < ntotal-1 && (nattained < level || ix[gx][0]==ix[gx+1][0])) {
				gx++;
				if (ix[gx][1] <= iy[gy][1]) {
					run = runtab[(ix[gx]-data)/nobj];
					if (!attained[run]++)
						nattained++;
				}
			}
#ifdef DEBUG
			for (i = 0; i < nruns; i++)
				fprintf(stderr, "%d ", attained[i]);
			fprintf(stderr, "\n");
#endif

			/* Now move down until desired attainment level is no longer reached */

			if (nattained >= level) {
				do {
					/* if there are repeated values along the y axis, we need to remember
					 * where we are
					 */
					save_nattained = nattained;
					memcpy(save_attained, attained, nruns * (sizeof *attained));
					do {
						if (iy[gy][0] <= ix[gx][0]) {
							run = runtab[(iy[gy]-data)/nobj];
							if (!--attained[run])
								nattained--;
						}
#ifdef DEBUG
						for (i = 0; i < nruns; i++)
							fprintf(stderr, "%d ", attained[i]);
						fprintf(stderr, "\n");
#endif
						gy++;
					} while (gy < ntotal && iy[gy][1]==iy[gy-1][1]);
				} while (nattained >= level && gy < ntotal);
				if (nattained < level) { /* this should always be the case... */
					if (coord != NULL) {
						fprintf(coord, "% .16e\t% .16e", ix[gx][0], iy[gy-1][1]);
						fprintf(coord, coord==indic ? "\t" : "\n");
					}
					if (indic != NULL) {
						fprintf(indic, "%d", save_attained[0]!=0);
						for (i = 1; i < nruns; i++)
							fprintf(indic, "\t%d", save_attained[i]!=0);
						fprintf(indic,"\n");
					}
				}
			}
		} while (gx < ntotal-1 && gy < ntotal);
		if (coord != NULL)
			fprintf(coord,"\n");
		if (indic != NULL && indic != coord)
			fprintf(indic,"\n");
	}
	free(save_attained);
	free(attained);
	free(runtab);
	free(iy);
	free(ix);
	return 0;
}
