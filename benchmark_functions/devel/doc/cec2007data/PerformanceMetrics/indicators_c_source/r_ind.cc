
/* r_ind.cc  (C) Joshua Knowles, January 2005

This program calculates Hansen and Jaszkiewicz's R1_R, R2_R and R3_R measures 
for evaluating the quality of sets of nondominated vectors WITH RESPECT TO
A REFERENCE SET R. 

For more information see sections below.

** Please contact me - Joshua Knowles - if you have any comments, suggestions
or questions regarding this program or metrics for measuring nondominated sets. 
My email address is j.knowles@manchester.ac.uk

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version. 

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details. 

   The GNU General Public License is available at:
      http://www.gnu.org/copyleft/gpl.html
   or by writing to: 
        The Free Software Foundation, Inc., 
        675 Mass Ave, Cambridge, MA 02139, USA.  


USAGE:
------

  To Compile:
    g++ r_ind.cc -o r_ind -lm

  *** Re-compiling this code with VERBOSE defined as 1 gives a verbose output to stderr ***



  To Run:
    ./r_ind [<paramfile>] <datafile> <referencefile> <outfile>


  <paramfile> specifies the name of a parameter file which must have the following format:

    dim <positive integer>
    obj <-|+> <-|+> ...
    ideal <double> <double> ...
    nadir <double> <double> ...
    rho <small positive double>
    method <1|2|3>
    R <{1|2|3>
    s <positive integer>

  where:

      dim specifies the number of objectives.
 
      obj specifies whether each objective is being minimized (-) or maximized (+)

      ideal gives the co-ordinates of an ideal reference point in the objective space.
       When comparing two sets of results, it is VERY IMPORTANT that
       the same reference point is used. The reference point must be better in all dimensions than
       the best value of any point in each dimension.

      nadir gives the co-ordinates of a nadir (or worst) point in the objective space.
       When comparing two sets of results, it is VERY IMPORTANT that
       the same reference point is used. The reference point must be worse in all dimensions than
       the worst value of any point in each dimension.

      rho is used in the calculation of the augmented Tchebycheff function. It controls the 
       degree to which the Thebycheff function is augmented by a weighted sum function. Values
       in the range 0.001 to 0.1 are normal. This number is ignored if the augmented Tchebycheff function is 
       not being used.

      method is an integer in {1,2,3} which specifies whether the weighted sum (1), Thebycheff (2), or
       augmented Thebycheff function (3) is used in the calculation of the R1 and R2 metrics.

      R specifies which of the R_R metrics to output.

      s is an integer parameter which controls the number of different weight vectors 
       that are used by the R metrics. An appropriate value of s depends on the value
       of dim, the number of objectives. The number of evenly distributed weight vectors
       is given by the equation |wv| = (s + dim - 1)choose(dim - 1).
       e.g.:
         dim=2, s=500, vectors = 501
         dim=3, s=30, vectors = 496
         dim=4, s=12, vectors = 455
         dim=5, s=8, vectors = 495
	 
    If the parameter file is omitted, default parameters are taken and
    the number of objectives is determined from the data file. The default parameters
    are:

         ideal 1 1 1 1 ... 1
	 nadir 2 2 2 2 ... 2
         rho   0.01
         method 3
	 R     2
         if dim=2 then s=500
         if dim=3 then s=30
         if dim=4 then s=12
         if dim=5 then s=8
	 else          s=3


  <datafile> specifies a file that contains the output of one or
  several runs of a selector/variator pair; the format corresponds to
  the one defined in the specification of the PISA monitor program.
  
  <referencefile> is the name of a file that contains the reference set
  according to which the indicator values are calculated; the file
  format is the same as for the data file.

  <output_file> defines the name of the file to which the computed
  indicator values are written to.


FURTHER INFORMATION:
--------------------

   This code calculates the R1_R, R2_R and R3_R measures of a set of points as described in
   Hansen and Jaszkiewicz 1998:

   @techreport{hansen98evaluating,
     author = "Michael Pilegaard Hansen and Andrzej Jaszkiewicz",
     title = "Evaluating the quality of approximations to the non-dominated set",
     number = "IMM-REP-1998-7",
     year = "1998",
     url = "citeseer.nj.nec.com/hansen98evaluating.html"}

   These measures were reviewed in the PhD thesis of Joshua Knowles and also in 
   a paper by Knowles and Corne published in the 2002 Congress on 
   Evolutionary Computation. These can
   both be downloaded from http://dbk.ch.umist.ac.uk/knowles/
   or from the EMOO webpage hosted by Carlos Coello Coello.

*******************************************************************************************/




#include <ctime>
#include <iostream>       
#include <iomanip>
#include <fstream>
#include <string>
#include <cmath>
#include <cstdio>
#include <cstdlib>

using namespace std;

#define LARGE 10e50
#define MAX_WVS 50000
#define MAX_LINE_LENGTH 1024
#define MAX_STR_LENGTH 200
#define VERBOSE 1
#define error(X,Y)  if (X) fprintf(stderr, Y "\n"), exit(1)

FILE *fp;

struct dnode{
  double *o;
  bool dominated;
  struct dnode *next;
  struct dnode *prev;
};


dnode **po;
dnode *ref;

double **wv;
int nsteps;
int *minmax;
double *ideal;
double *nadir;
double rho;
int s;
int Rmet;
int method;
double **mymax;

int nobjs=2;

double myabs(double a);
double weighted_sum(double *wv, double *x, double *ideal, double *nadir);
double Tchebycheff(double *wv, double *x, double *ideal, double *nadir);
double augTcheby(double *wv, double *x, double *ideal, double *nadir);
void create_vectors(double **wv, const int s, const int k);
inline void int2kary(int x, const int basek, const int digits, int *kary);
inline int mypow(int x, int exp);
int choose(const int n, const int r);
int factorialdiff(const int n, const int r);
int factorial(const int n);
void d_append (struct dnode **s, double *vec);
void d_display ( struct dnode *q );
void  check_file(FILE  *fp, int  *no_runsp, int  *max_pointsp);
int  determine_dim(FILE  *fp);
void  read_file(FILE  *fp, int  *no_pointsp, dnode **po);

void d_append (struct dnode **s, double *vec) 
/* adds a new node at the end of the doubly linked list */ 
{
  struct dnode *r, *q = *s ; 
  /* if the linked list is empty */ 
  
  if ( *s == NULL ) 
    { 
      
      /*create a new node */ 
      
      *s = (dnode *) malloc ( sizeof ( struct dnode ) ) ;
      ( *s ) -> prev = NULL ; 
      (*s)->o = (double *) malloc(nobjs * sizeof (double) );
      for(int j=0;j<nobjs;j++)
	( *s ) -> o[j] = vec[j]; 
      (*s)->dominated = false;
      ( *s ) -> next = NULL ; 
    } 
  
  else 
    { 
      
      /* traverse the linked list till the last node is reached */ 
      while ( q -> next != NULL ) 
	q = q -> next; 

      /* add a new node at the end */ 

      r = (dnode *)malloc ( sizeof ( struct dnode ) ) ; 
      r->o = (double *)malloc(nobjs * sizeof (double) );
      for(int j=0;j<nobjs;j++)
	r -> o[j] = vec[j]; 
      r -> next = NULL; 
      r -> prev = q; 
      q -> next = r; 
    } 
  
} 

/* displays the contents of the linked list */ 
void d_display ( struct dnode *q ) 
{ 
  /* traverse the entire linked list */ 
  while ( q != NULL )     
    { 
      if(1) // (q->dominated==false)
	{
	  for(int j=0;j<nobjs;j++)
	    printf("%g ", q->o[j]);
	  printf("\n");
	}
      q = q -> next;       
    } 
  
} 


int main(int argc, char **argv)
{
  int num;
  int run;
  int nruns;
  int ref_set_size;
  int i;  
  double val, maxim;
  double aval,bval,atotal;
  double total_val=0.0;
  double mean_val;
  char str[MAX_STR_LENGTH];

  error(argc!=5 && argc != 4,
	"./r_ind [<paramfile>] <datafile> <referencefile> <outfile>");
  
  
  /* read in the parameter file */
  if (argc == 5) {
      if((fp = fopen(argv[1], "rb")))
      {
	  fscanf(fp, "%s", str);
	  error(strcmp(str, "dim") != 0, "error in parameter file");
	  fscanf(fp, "%d", &nobjs);
	  fscanf(fp, "%s", str);
	  error(strcmp(str, "obj") != 0, "error in parameter file");
	  minmax = (int *)malloc(nobjs*sizeof(int));
	  for (i = 0; i < nobjs; i++) 
	  {
	      fscanf(fp, "%s", str);
	      error(str[0] != '-' && str[0] != '+', "error in parameter file");
	      if (str[0] == '-')
		  minmax[i] = -1;
	      else
		  minmax[i] = 1;
	  }
	  ideal = (double *)malloc(nobjs*sizeof(double));
	  nadir = (double *)malloc(nobjs*sizeof(double));
	  fscanf(fp, "%s", str); error(strcmp(str, "rho") != 0, "error in parameter file");
	  fscanf(fp, "%lf\n", &rho);
	  fscanf(fp, "%s", str); error(strcmp(str, "method") != 0, "error in parameter file");
	  fscanf(fp, "%d\n", &method);
	  fscanf(fp, "%s", str);error(strcmp(str, "R") != 0, "error in parameter file");
	  fscanf(fp, "%d\n", &Rmet);
	  fscanf(fp, "%s", str); error(strcmp(str, "s") != 0, "error in parameter file");
	  fscanf(fp, "%d\n", &s);
	  fscanf(fp, "%s", str);error(strcmp(str, "nadir") != 0, "error in parameter file");
	  for(i=0;i<nobjs;i++)
	      error((fscanf(fp, "%lf", &(nadir[i]))!=1),"error in parameter file");
	  fscanf(fp, "%s", str);error(strcmp(str, "ideal") != 0, "error in parameter file");
	  for(i=0;i<nobjs;i++)
	      error((fscanf(fp, "%lf", &(ideal[i]))!=1),"error in parameter file");
	  fclose(fp);
      }
      else
      {
	  fprintf(stderr,"Couldn't open param file\n");
	  exit(1);
      }
  }
  else {
	fp = fopen(argv[1], "r");
	error(fp == NULL, "data file not found");
	nobjs = determine_dim(fp);
	error(nobjs < 1, "error in data file");
	fclose(fp);
	minmax = (int *)malloc(nobjs*sizeof(int));
	ideal = (double *)malloc(nobjs*sizeof(double));
	nadir = (double *)malloc(nobjs*sizeof(double));
	for (i = 0; i < nobjs; i++) {
	    nadir[i] = 2.0;
	    ideal[i] = 1.0;
	    minmax[i] = -1;
	}
	rho = 0.01;
	method = 3;
	Rmet = 2;
	switch (nobjs) {
	case 2:
		s = 500;
		break;
	case 3:
		s = 30;
		break;
	case 4:
		s = 12;
		break;
	case 5:
		s = 8;
		break;
	default:
		s = 3;
		break;
	}
  }
  
  error(((Rmet>3)||(Rmet<1)), "Error: R must be 1,2 or 3");
  error(((method>3)||(method<1)), "Error: method must be 1,2 or 3");
  
  nsteps = choose(s+nobjs-1,nobjs-1);
  error((nsteps>MAX_WVS), "Error: R must be 1,2,or 3");

  error(((rho<0)||(rho>1)), "Error: rho must be in [0,1]");

  if((wv = (double **)malloc(nsteps * sizeof(double *)))==NULL)
    {
      fprintf(stderr, "Malloc error. Exiting\n");
      exit(1);
    }

  for(i=0;i<nsteps;i++)
    {
      if((wv[i] = (double *)malloc(nobjs * sizeof(double)))==NULL)
	{
	  fprintf(stderr, "Malloc error. Exiting\n");
	  exit(1);
	}
    }
  
  create_vectors(wv,s,nobjs);  // sets up the lambda weight vectors used in the utility functions



  /* read in reference set file */
  fp = fopen(argv[(argc == 5 ? 3 : 2)], "r");
  error(fp == NULL, "reference set file not found");
  check_file(fp, &nruns, &num);
  error(nruns != 1 || num < 1, "error in reference set file");
  rewind(fp);
  ref=NULL;
  read_file(fp, &ref_set_size, &ref);
  fclose(fp);


  /* read in each of the approximation sets */
  if((fp=fopen(argv[(argc == 5 ? 2 : 1)], "rb")))
    {
      check_file(fp, &nruns, &num);
      rewind(fp);

      po = (dnode **)malloc(nruns*sizeof(dnode *));
      
      int j=0;
      while(j<nruns)
	{
	  po[j]=NULL;
	  read_file(fp, &num, &(po[j]));      
	  d_display(po[j]);
	  j++;
	}
    }
  else
    {
      fprintf(stderr,"Couldn't open %s", argv[(argc == 5 ? 2 : 1)]);
      exit(0);
    }

  
  /*compute the R indicators */
  mymax = (double **)malloc((nruns+1)*sizeof(double *));
  for(i=0;i<nruns+1;i++)
    mymax[i] = (double *)malloc((nsteps)*sizeof(double));

  struct dnode *pp;
  for(run=0;run<nruns+1;run++)
    {
      total_val=0;
      for(i=0;i<nsteps;i++)
	{
	  maxim=-LARGE;
	  if(run<nruns)
	    pp = po[run];
	  else
	    pp = ref;
	  while(pp!=NULL)
	    {
	      //printf("method=%d\n",method);
	      if(method==1)
		val=weighted_sum(wv[i], pp->o, ideal, nadir);
	      else if(method==2)
		val=Tchebycheff(wv[i], pp->o, ideal, nadir);
	      else
		val= augTcheby(wv[i], pp->o, ideal, nadir);
	      //printf("val=%g\n", val);
	      if(val>maxim)
		{       
		  mymax[run][i]=maxim=val;
		}
	      pp = pp->next;
	    }
	  total_val+=maxim;
	}
      mean_val = total_val/nsteps;
      if(run<nruns)
	{
	  if(VERBOSE)fprintf(stderr,"Mean utility = %f for run %d\n", mean_val, run+1);
	}
      else
	if(VERBOSE)fprintf(stderr,"Mean utility = %f for reference set\n", mean_val);
	  
    }

  if((fp = fopen(argv[(argc == 5 ? 4 : 3)],"wb")))
    {
      if(Rmet==1)
	{
	  if(VERBOSE)
	    fprintf(stderr,"R1_R gives the fraction of utility functions on which the approximation set\nA is better than the reference set R. 1 is the best result. 0 is the worst. Note: if all approximation sets\nscore 1 then this does not imply that they are equally good; the reference set is too poor. Analogously,\nif all approximations score 0, the reference set is too good. Try using R2_R or R3_R.\n");
	  for(run=0;run<nruns;run++)
	    {
	      atotal=0.0;
	      for(i=0;i<nsteps;i++)
		{
		  aval=mymax[run][i];
		  bval=mymax[nruns][i];
		  
		  if(aval > bval)
		    atotal+=1.0;
		  else if (bval == aval)
		    atotal+=0.5;
		}
	      atotal/=nsteps;
	      fprintf(fp, "%.9e\n", atotal);
	      
	      if(VERBOSE)
		fprintf(stderr,"%.9e\n", atotal);
	    }
	}
      
      else if(Rmet==2)
	{
	  if(VERBOSE)
	    fprintf(stdout,"R2_R gives the average of the utility difference u(R)-u(A). Lower values are better,\nwith -1 being best, and +1 being worst.\n");
	  
	  for(run=0;run<nruns;run++)
	    {
	      atotal=0.0;
	      for(i=0;i<nsteps;i++)
		{
		  atotal+=mymax[nruns][i]-mymax[run][i];
		}
	      atotal/=nsteps;
	      fprintf(fp,"%.9e\n", atotal);
	      if(VERBOSE)
		fprintf(stderr, "%.9e\n", atotal);
	      
	    }
	  
	}

      else if(Rmet==3)
	{
	  // R3 gives the average of the utility ratio u(R)-u(A) / u(R)
	  // As such, lower values are better, with -infinity being best, 
	  // and higher values are worse, with +infinity being worst.
	  
	  if(VERBOSE)
	    fprintf(stderr, "R3_R gives the average of the utility ratio u(R)-u(A) / u(R)\nAs such, lower values are better, with -infinity being best, \nand higher values are worse, with +infinity being worst.\n");
	  for(run=0;run<nruns;run++)
	    {
	      atotal=0.0;
	      for(i=0;i<nsteps;i++)
		{
		  atotal+=(mymax[nruns][i]-mymax[run][i])/(mymax[nruns][i]+1e-30);
		  //printf("%d %.9e %.9e\n", run, mymax[run][i],mymax[nruns][i] );
		}
	      atotal/=nsteps;
	      fprintf(fp, "%.9e\n", atotal);
	      
	      if(VERBOSE)
		fprintf(stderr, "%.9e\n", atotal);
	    }      
	}
      fclose(fp);
    }
  else
    {
      fprintf(stderr, "Couldn't open output file %s for writing\n", argv[(argc == 5 ? 4 :3)]);
      exit(1);
    }





  return(0);
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
	    for (j = 1; j < nobjs; j++) {
		while (line[i] != ' ' && line[i] != '\n' && line[i] != '\0')
		    i++;
		if((sscanf(&(line[i]), "%lf", &number)) <= 0)
		  {
		    fprintf(stderr,"error in data or reference set file");
		    exit(0);
		  }
		
		while (line[i] == ' ' && line[i] != '\0')
		    i++;
	    }
	    no_points++;
	}
    }
    if (*max_pointsp < no_points)
	*max_pointsp = no_points;
}

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
	    while (line[i] != ' ' && line[i] != '\n' && line[i] != '\0')
		i++;
	    number_found = sscanf(&(line[i]), "%lf", &number);
	    while (line[i] == ' ' && line[i] != '\0')
		i++;
	} while (number_found == 1);
    }
    
    return no_obj;
}

void  read_file(FILE  *fp, int  *no_pointsp, dnode **po)
{
  char  line[MAX_STR_LENGTH];
  int  i, j, k;
  int  reading;
  double  number;
  double *vector;

  vector=(double *)malloc(nobjs*sizeof(double));
  
  reading = 0;
  *no_pointsp = 0;
  //  printf("read_file\n");
  while (fgets(line, MAX_LINE_LENGTH, fp) != NULL) 
    {
      //      printf("OK\n");
      k=0;
      if (sscanf(line, "%lf", &number) != 1) {
	if (reading)
	{
	  printf("break\n");
	  break;
	}
      }
      else 
	{
	  reading = 1;
	  vector[k++] = number;
	  i = 0;
	  for (j = 1; j < nobjs; j++) 
	    {
	      while (line[i] != ' ' && line[i] != '\n' && line[i] != '\0')
		i++;
	      if((sscanf(&(line[i]), "%lf", &number)) <= 0)
		{
		    fprintf(stderr,"error in data or reference set file");
		    exit(0);
		}
	      
	      vector[k++] = number;
	      while (line[i] == ' ' && line[i] != '\0')
		i++;
	    }
	  (*no_pointsp)++;
	  // printf("%.9e", vector[0]);
	  d_append(po, vector);
	}
    } 
}


double weighted_sum(double *wv, double *x, double *ideal, double *nadir)
{
  /* Returns the linear weighted sum utility of x on weight vector wv. The vector x is first */
  /* normalized with respect to the ideal and nadir points. The utility                      */
  /* returned is positive, with larger values indicating greater utility                     */
  int i;
  double total=0.0;
  
  for(i=0;i<nobjs;i++)
    {
      if(minmax[i]==-1)
	error(((x[i]<ideal[i])||(x[i]>nadir[i])),"Error: ideal must be better and nadir must be worse than all points");
      else if(minmax[i]==1)
	error(((x[i]>ideal[i])||(x[i]<nadir[i])),"Error: ideal must be better and nadir must be worse than all points");
    }
  
  for(i=0;i<nobjs;i++)
    {
      total += wv[i]*(1.0-((myabs(ideal[i]-x[i]))/(myabs(ideal[i]-nadir[i])))); 
    }
  return(total);
}
 
double Tchebycheff(double *wv, double *x, double *ideal, double *nadir)
{
  /* Returns the Tchebycheff utility of x on weight vector wv. The vector x is first */
  /* normalized with respect to the ideal and nadir points. The utility              */
  /* returned is positive, with larger values indicating greater utility             */

  int i;
  double val;
  double mymax=0.0;

  for(i=0;i<nobjs;i++)
    {
      if(minmax[i]==-1)
	error(((x[i]<ideal[i])||(x[i]>nadir[i])),"Error: ideal must be better and nadir must be worse than all points");
      else if(minmax[i]==1)
	error(((x[i]>ideal[i])||(x[i]<nadir[i])),"Error: ideal must be better and nadir must be worse than all points");
    }

  for(i=0;i<nobjs;i++)
    {
      val = wv[i]*(1.0-((myabs(ideal[i]-x[i]))/(myabs(ideal[i]-nadir[i])))); 
      if(val>mymax)
	mymax=val;
    }
  return(mymax);
}

double augTcheby(double *wv, double *x, double *ideal, double *nadir)
{
  /* Returns the augmented Tchebycheff utility of x on weight vector wv. The vector x is first */
  /* normalized with respect to the ideal and nadir points. The utility                        */
  /* returned is positive, with larger values indicating greater utility                       */

  double result;
  result = Tchebycheff(wv, x, ideal, nadir) + rho*weighted_sum(wv,x,ideal,nadir);
  return(result);
}


void create_vectors(double **wv, const int s, const int k)
{
  int i,j;
  int c=0;
  int num, sum;
  int *count;
  count = (int *)malloc(k*sizeof(int));
  num = choose(s+k-1,k-1);

  i=0;
  while(i<mypow(s+1,k))
    {
      sum=0;
     
      int2kary(i,s+1,k,count);
      for(j=0;j<k;j++)
	{
	  sum+=count[j];
	}
      if(sum==s)
	{
	  for(j=0;j<k;j++)
	    {
	      wv[c][j]=(double)count[j]/(double)s;
	    }
	  c++;
	}      
      i++;
    }
}

inline void int2kary(int x, const int basek, const int digits, int *kary)
{
  int i;
  int val;
  if (x>=mypow(basek,digits))
    {
      printf("Number in int2kary() too large. Exiting.\n");
      exit(-1);
    }
  val=digits-1;

  for(i=0;i<digits;i++)
    kary[i]=0;

  i=0;
  while(x)
    {
      if(x>=mypow(basek,val))
	{
	  kary[i]+=1;
	  x-=mypow(basek,val);
	}
      else 
	{
	  val-=1;
	  i++;
	}
    }
}

inline int mypow(int x, int exp)
{
  int i;
  int var=1;
  for(i=0;i<exp;i++)
    var*=x;
  return(var);
}

int choose(const int n, const int r)
{
  return(factorialdiff(n,r)/factorial(r));
}

int factorialdiff(const int n, const int r)
{
  //this function calculates n!/(n-r)!
  int i;
  int result=1;
  for(i=n;i>(n-r);i--)
    {
      result*=i;
    }
  return(result);
  
}

int factorial(const int n)
{
  if(n>11)
    {
      printf("too big number to factorial!\n");
      exit(1);
    }
  int i;
  int result=1;
  for(i=n;i>1;i--)
    {
      result*=i;
    }
  return(result);
}

double myabs(double a)
{
  if(a>=0)
    return(a);
  else
    return(-a);
}
