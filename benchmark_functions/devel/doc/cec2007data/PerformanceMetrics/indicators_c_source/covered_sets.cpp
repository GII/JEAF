/*===========================================================================*
 * covered_sets.c: implements the covered sets measure from the EMO 2007 paper
 *		Capabilities of EMOA to Detect and Preserve Equivalent Pareto Subsets
 *		(Rudolph, Naujoks, Preuss)
 *
 * Compile:
 *   g++ -lm -o covered_sets covered_sets.cpp
 *
 * Usage:
 *   covered_sets [<param_file>] <data_file> <output_file>
 *
 *   <data_file> specifies a file that contains the output of one or
 *     several runs of a selector/variator pair; the format corresponds to
 *     the one defined in the specification of the PISA monitor program.
 *
 *   <output_file> defines the name of the file to which the computed
 *     indicator values are written to.
 *
 *
 * Author:
 *   Mike Preuss, gratefully acknowledging the code template by
 *   Eckart Zitzler, using additional code fragments by Guenter Rudolph
 *
 * Changes:
 * 	 February 5, 2007 */


#include <float.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define error(X,Y)  if (X) fprintf(stderr, Y "\n"), exit(1)

#define MAX_LINE_LENGTH  2048 /* maximal length of lines in the files */
#define MAX_STR_LENGTH  2560 /* maximal length of strings in the files */

#define SYM_PARTS 9 /* number of Pareto subsets */

int  dim;  /* number of seach space dimensions */


inline double square(double x) {
    return x*x;
}

/* geometry constants for the symmetric parts */
#define a 1
#define b 10
#define c 8
#define c1 (a+c/2)
#define c2 (c+2*a)
#define b1 (b/2)

/* these variables are needed to speed up rotation */
double angle = 45.;
static double pi = atan(1.) * 4;
static double omega = angle * pi / 180.;
static double si = sin(omega), co = cos(omega);

/* rotates the whole field in groups of 2, that is dimensions
   0-1, 2-3, and so on */
inline void rotate(int xDim, double *x) {
	double h1;
	for( int dim=0; dim+1 < xDim; dim+=2 ) {
		h1 = x[dim];
		x[dim] = co * h1 - si * x[dim+1];
		x[dim+1] = si * h1 + co * x[dim+1];
	}
}


/* determines tile from first two decision space dimensions,
   returns -1 if outside of the symmetric parts, otherwise 0-8 */
int findTile(double x1, double x2) {

	double xx1 = (x1 < 0) ? -x1 : x1;
	double xx2 = (x2 < 0) ? -x2 : x2;
	int t1, t2;

	t1 = (xx1 < c1) ? 0 : ((int)ceil((xx1-c1)/c2));
	t2 = (xx2 < b1) ? 0 : ((int)ceil((xx2-b1)/b));

	if (x1 < 0) t1 = -(t1);
	if (x2 < 0) t2 = -(t2);

	//printf( "coords: ( %g , %g ), t1: %d , t2: %d \n", x1, x2, t1, t2 );

 	// test if outside
	if( (abs(t1) > 1) || (abs(t2) > 1) ) return -1;

	// or inside
	return (t2+1) * 3 + (t1+1);
}


/* determines the number of tiles covered by the given population */
double  getPopValue(double  *completePop, int popSize) {

	int  i, part;
    double  answer = 0;
	double  tmp[2];

	// field for counting up touched parts
	int counts[SYM_PARTS];
	for(i = 0; i < SYM_PARTS; i++ ) counts[i] = 0;
	//for(i = 0; i < popSize; i++ ) printf( "current pop: %g \n", completePop[i] );

    for (i = 0; i < popSize; i++) {
		tmp[0] = completePop[i * dim];
		tmp[1] = completePop[i * dim + 1];
		rotate( 2, tmp );
		part = findTile( tmp[0], tmp[1] );
		//printf( "rotated coords: ( %g , %g ), part: %d \n", tmp[0], tmp[1], part );
		if( part>=0 ) counts[part]++;
    }
    for(i = 0; i < SYM_PARTS; i++)
		if( counts[i] > 0 ) answer ++;

	return answer;
}


/* currently unused as no parameters are needed */
void  read_params(FILE  *fp)
{
    char str[MAX_STR_LENGTH];
    int  i;

    //fscanf(fp, "%s", str);
    //error(strcmp(str, "dim") != 0, "error in parameter file");
    //fscanf(fp, "%d", &dim);
    //error(dim <= 0, "error in parameter file");

}

void  check_file(FILE  *fp, int  *no_runsp, int  *max_pointsp)
    /* determines the maximum number of points and the number of runs
       for the data resp. the reference set file; if the array v is
       specified, the data read in will be stored in v
    */
{
    char  line[MAX_STR_LENGTH];
    int  i, j;
    int  new_run;
    int  no_points;
    double  number;

    no_points = 0;
    *max_pointsp = 0;
    *no_runsp = 0;
    new_run = 1;
    while (fgets(line, MAX_LINE_LENGTH, fp) != NULL) {
	if (sscanf(line, "%lf", &number) != 1)
	    new_run = 1;
	else {
	    if (new_run == 1)
	    {
		(*no_runsp)++;
		if (*max_pointsp < no_points)
		    *max_pointsp = no_points;
		no_points = 0;
	    }
	    new_run = 0;
	    i = 0;
	    for (j = 1; j < dim; j++) {
		while (line[i] != ' ' && line[i] != '\n' && line[i] != '\0')
		    i++;
		error(sscanf(&(line[i]), "%lf", &number) <= 0,
		      "error in data or reference set file");
		while (line[i] == ' ' && line[i] != '\0')
		    i++;
	    }
	    no_points++;
	}
    }
    if (*max_pointsp < no_points)
	*max_pointsp = no_points;
}


/* determine the number of numbers in a data file line */
int  determine_dim(FILE  *fp)
{
    char  line[MAX_STR_LENGTH];
    int  i, no_obj;
    int  line_found, number_found;
    double  number;

    no_obj = 0;
    line_found = 0;
    while (fgets(line, MAX_LINE_LENGTH, fp) != NULL && !line_found)
        line_found = sscanf(line, "%lf", &number);
    if (line_found) {
		i = 0;
		do {
		    no_obj++;
			// throw away leading whitespace
		    while ((line[i] != '\0') && 
		    		((line[i] == ' ') || (line[i] == '\t')) ) i++;
			// scan number
		    number_found = sscanf(&(line[i]), "%lf", &number);
		    //if( number_found>=1 ) printf( "number decoded: %g \n", number );
		    //else printf( "not a number, remaining string:%s \n", &(line[i]) );
			if( number_found<=0 ) no_obj--;
			// read over number chars
 		    while (line[i] != ' ' && line[i] != ':' && line[i] != '\t'
		    && line[i] != '\n' && line[i] != '\0') i++;		    
		    //printf( "remaining line:%s \n", &(line[i]) );
		} while ( (number_found == 1) && (line[i] != '\0') );
    }

    return no_obj;
}

void  read_file(FILE  *fp, int  *no_pointsp, double  *points)
{
    char  line[MAX_STR_LENGTH];
    int  i, j, k;
    int  reading;
    double  number;

    k = 0;
    reading = 0;
    *no_pointsp = 0;
    while (fgets(line, MAX_LINE_LENGTH, fp) != NULL) {
		if (sscanf(line, "%lf", &number) != 1) {
		    if (reading) break;
		} else {
		    reading = 1;
		    points[k++] = number;
		    i = 1;
		    for (j = 1; j < dim; j++) {
				// throw away leading whitespace
			    while ((line[i] != '\0') && 
		    		((line[i] == ' ') || (line[i] == '\t')) ) i++;
				// throw away number chars
				while (line[i] != ' ' && line[i] != ':' && line[i] != '\t'
					&& line[i] != '\n' && line[i] != '\0') i++;
				// scan number
				error(sscanf(&(line[i]), "%lf", &number) <= 0,
			      "rf: error in data file");
				points[k++] = number;
				//printf( "number	read: %lf \n", number );
		    }
		    (*no_pointsp)++;
		    //printf( "end block\n" );
		}
    }
}

int  main(int  argc, char  *argv[])
{
    int  i;
    int  no_runs;  /* number of runs */
    int  runsCountDown;  /* counting variable for number of runs */
    int  max_points;  /* maximum number of points per run */
    int  curr_run_size;  /* number of points associated with the current run */
    double  *curr_run; /* objective vectors fur current run */
    double  pop_value;
    double  mean = -1;
    FILE  *fp, *out_fp;

    error(argc != 3 && argc != 4,
	  "Covered-sets indicator - wrong number of arguments:\n"\
	  "covered_sets [parFile] dataFile outFile");

    /* set parameters, currently ignored */
    if (argc == 4) {
		fp = fopen(argv[1], "r");
		error(fp == NULL, "parameter file not found");
		read_params(fp);
		fclose(fp);
    } else {
		// determine dimensions from data file
		fp = fopen(argv[1], "r");
		error(fp == NULL, "data file not found");
		dim = determine_dim(fp);
		error(dim < 1, "dim: error in data file");
		fclose(fp);
		printf("%d search space dimensions detected\n", dim);
	}

    /* check data file */
    if (argc == 4)
	fp = fopen(argv[2], "r");
    else
	fp = fopen(argv[1], "r");
    error(fp == NULL, "data file not found");
    check_file(fp, &no_runs, &max_points);
	printf("%d runs, max %d points\n", no_runs, max_points);
    error(no_runs < 1 || max_points < 1, "noinfo: error in data file");
    curr_run = (double *)malloc(dim * max_points * sizeof(double));
    rewind(fp);

    /* process data */
    if (argc == 4)
	out_fp = fopen(argv[3], "w");
    else
	out_fp = fopen(argv[2], "w");
    error(out_fp == NULL, "output file could not be generated");
    runsCountDown = no_runs;
    while (runsCountDown > 0) {
		read_file(fp, &curr_run_size, curr_run);
		pop_value = getPopValue(curr_run, curr_run_size);
		if( mean==-1 ) mean = pop_value; else mean += pop_value;
		fprintf(out_fp, "%.9e\n", pop_value);
		runsCountDown--;
    }
    printf( "covered sets mean value: %g \n", mean/no_runs);
    fclose(out_fp);
    fclose(fp);
}
