
/* normalize.cc  (C) Joshua Knowles 2005

A program that reads in a file <datafile>, consisting of a collection
of approximation sets, assumed to be from one optimizer. 
A separate parameter file <param> gives 
the dimension of the data and whether each objective should
be minimized or maximized. It also gives the upper and lower 
bound of each objective to normalize to. Finally, it specifies whether
to unify all the objectives so that they are to be all minimized or 
all maximized, or not to unify them in this way.
The output is the collection of points normalized so that they lie
within the range 1,2 in each dimension and appropriately unified.


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

   


   COMPILE:
      g++ normalize.cc -o normalize -lm
      
   RUN:
      ./normalize [<paramfile>] <boundfile> <datafile> <outfile>
      
    
    The format of the parameter file <param> is

      dim <number>
      obj <+|-> <+|-> ...
      unify <min|max|no>

      where dim specifies the number of objective dimensions
      obj specifies whether each objective should be minimized (-) or maximized (+)
      unify specifies whether to unify objectives to all-minimization, to all-maximization, or not at all
      If the parameter file is omitted, default parameters are taken and
      the number of objectives is determined from the data file. 


   The bound file must contain two lines of the format

      upper_bound <number> <number>... <number>
      lower_bound <number> <number>... <number>

      where the order of the lines is arbitrary; any other contents of the file
      will be disregarded.
      upper_bound is a point that is larger in each dimension than any point in the datafile
      lower_bound is a point that is smaller in each dimension than any point in the datafile
      
   The format of the collection of approximation sets in <datafile> is
      <number> <number> ...
      <number> <number> ...
      [blank line]
      <number> <number> ...
      <number> <number> ...
      [blank line]
      .
      .
      <number> <number> ...

      
     [ Note: blank lines have no effect - since all points
      are considered together. ]


    The output of normalize is a file <outfile> of the normalized and unified values.

*******************************************************************/



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


dnode **p;

int *minmax;
double *ubound;
double *lbound;
double phi;
int nobjs;


double myabs(double a);
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
      r->dominated = false;
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
  int nruns;
  int i;  
  char str[MAX_STR_LENGTH];
  char unify[MAX_STR_LENGTH];
  
  error(argc!=5 && argc != 4,
	"./normalize [<paramfile>] <boundfile> <datafile> <outfile>");
  
  
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
	  fscanf(fp, "%s", str);error(strcmp(str, "unify") != 0, "error in parameter file");
	  fscanf(fp, "%s", unify);
	  error(strcmp(unify, "min")!=0 && strcmp(unify, "max")!=0 && strcmp(unify, "no")!=0, "error in parameter file");
	  fclose(fp);
      }
      else
      {
	  fprintf(stderr,"Couldn't open param file\n");
	  exit(1);
      }
  }
  else {
      	fp = fopen(argv[2], "r");
	error(fp == NULL, "data file not found");
	nobjs = determine_dim(fp);
	error(nobjs < 1, "error in data file");
	fclose(fp);
	minmax = (int *)malloc(nobjs*sizeof(int));
	for (i = 0; i < nobjs; i++) 
	    minmax[i] = -1;
	sprintf(unify, "no");
  }
  
  if((fp=fopen(argv[(argc == 5 ? 2 : 1)], "rb")))
  {
      ubound = (double *)malloc(nobjs*sizeof(double));
      lbound = (double *)malloc(nobjs*sizeof(double));
      do {
	  fscanf(fp, "%s", str);
      } while (strcmp(str, "upper_bound") != 0 && !feof(fp));
      error(feof(fp), "error in bound file");
      for(i=0;i<nobjs;i++)
	error((fscanf(fp, "%lf", &(ubound[i]))!=1),"error in parameter file");
      rewind(fp);
      do {
	  fscanf(fp, "%s", str);
      } while (strcmp(str, "lower_bound") != 0 && !feof(fp));
      error(feof(fp), "error in bound file");
      for(i=0;i<nobjs;i++)
	error((fscanf(fp, "%lf", &(lbound[i]))!=1),"error in parameter file");
       fclose(fp);
  }
  else
  {
      fprintf(stderr,"Couldn't open bound file\n");
      exit(1);
  }
     
  /* read in each of the approximation sets */
  if((fp=fopen(argv[(argc == 5 ? 3 : 2)], "rb")))
    {
      check_file(fp, &nruns, &num);
      rewind(fp);
      p=(dnode **)malloc(nruns*sizeof(dnode *));
      for(int j=0;j<nruns;j++)
	{
	  p[j]=NULL;
	  read_file(fp, &num, &(p[j]));      
	}
    }
  else
    {
      fprintf(stderr,"Couldn't open %s", argv[(argc ==5 ? 3 : 2)]);
      exit(1);
    }
  

  if(!(fp=fopen(argv[(argc == 5 ? 4 : 3)],"wb")))
    {
      fprintf(stderr, "Couldn't open %s for writing. Exiting\n",
	      argv[(argc == 5 ? 4 : 3)]);
      exit(1);
    }
  for(i=0; i<nruns;i++)
    {
      struct dnode *di = p[i];
      while( di !=NULL)
	{
	  for(int j=0;j<nobjs;j++)
	    {
	      if(((minmax[j]==1)&&(strcmp(unify,"max")==0))||((minmax[j]==-1)&&(strcmp(unify,"min")==0)))
		fprintf(fp, "%.9e ", 1.0+(di->o[j]-lbound[j])/(ubound[j]-lbound[j]));
	      else if(((minmax[j]==1)&&(strcmp(unify,"min")==0))||((minmax[j]==-1)&&(strcmp(unify,"max")==0)))
		fprintf(fp, "%.9e ", 1.0+(ubound[j]-di->o[j])/(ubound[j]-lbound[j])); // reverses the sense
	      else
		fprintf(fp, "%.9e ", 1.0+(di->o[j]-lbound[j])/(ubound[j]-lbound[j]));
	    }
	  fprintf(fp, "\n");	  
	  di=di->next;
	}
      if(i<nruns-1)
	fprintf(fp, "\n");
    }
  fclose(fp);
  exit(0);
  return(0);

}

void  check_file(FILE  *fp, int  *no_runsp, int  *max_pointsp)
    /* check_file() function by Eckart Zitzler
       determines the maximum number of points and the number of runs
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
  /* read_file() function by Eckart Zitzler */
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
      if (sscanf(line, "%lf", &number) != 1)
	{
	  //	  printf("break\n");
	  if (reading) break;
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
	  // printf("%g", vector[0]);
	  d_append(po, vector);
	}
    } 
}

double myabs(double a)
{
  if(a>=0)
    return(a);
  else
    return(-a);
}



