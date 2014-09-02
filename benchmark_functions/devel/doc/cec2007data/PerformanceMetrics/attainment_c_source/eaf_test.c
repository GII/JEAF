#include <stdlib.h>
#include <stdio.h>

#define BPU (8*sizeof(unsigned))


/*
 * The number of ones in an integer between 0 and 255
 */

static int bits[] = {0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4, \
                     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5, \
                     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7, \
                     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7, \
                     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6, \
                     3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7, \
                     3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7, \
                     4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8};

/*
 * Compute the number of ones in a bitmap
 */

static int bit_weight(unsigned x)
{
    int i,d=0;
    for (i = 0; i < sizeof(unsigned); i++) {
#ifdef DEBUG
        printf("%x, %d, %d\n", (x >> (i<<3)) & 0xff, bits[(x >> (i<<3)) & 0xff], d);
#endif
	d += bits[(x >> (i<<3)) & 0xff];
    }
    return d;
}

/*
 * Generate an array of m random n-bit masks with p ones each
 */

static unsigned *randmask(int m, int n, int p)
{
    int *s, i, j, k, cols, tmp;
    unsigned *rm;

    cols = (n - 1) / BPU + 1;
    rm = calloc(m, cols * sizeof(unsigned));
    s = malloc(n * sizeof(*s));

    for (j = 0; j < n; j++)
	s[j] = j;

    for (i = 0; i < m; i++) {
	for (j = 0; j < (n - 1); j++) {
	    k = (int) ((double) (n - j) * rand() / (RAND_MAX + 1.0));
	    tmp = s[j + k];
	    s[j + k] = s[j];
	    s[j] = tmp;
	}
	/* This loop should be merged with the previous one */
	for (j = 0; j < p && j < n; j++)
	    rm[i*cols+s[j]/BPU] |= 1 << (s[j] % BPU);	
    }
    free(s);
    return rm;
}


int eaf_ks_stat(int *data, int nvars, int npoints)
{
    int i, j, k, l, maxd=0, d, cols;

    unsigned *dmap, *c, *mask;

    cols = (nvars - 1) / BPU + 1;
    dmap = calloc(npoints, cols * sizeof(unsigned));
    c = calloc(cols, sizeof(unsigned));
    mask = calloc(cols, sizeof(unsigned));

    for (j = 0; j < cols; j++)
    	for (k = 0; k < BPU && (l = j * BPU + k) < nvars/2; k++)
	    mask[j] |= (1<<k);

    for (i = 0; i < npoints; i++) {
	for (j = 0; j < cols; j++)
	    for (k = 0; k < BPU && (l = j * BPU + k) < nvars; k++)
		dmap[i * cols + j] |= (data[i * nvars + l]!=0) << k;
    }

    for (i = 0; i < npoints; i++) {
	d = 0;
	for (k = 0; k < cols; k++) {
	    c[k] = dmap[i * cols + k];
	    d += bit_weight(c[k] & mask[k]) - bit_weight(c[k] & ~mask[k]);
	}
	if (maxd < abs(d))
	    maxd = abs(d);
    }
    free(dmap);
    free(c);
    free(mask);
    return maxd;
}


int *eaf_ks_tail(int *data, int nvars, int npoints, double alpha, int nperms)
{
    int i, j, k, l;
    int n;
    int w, *D, *tail, critv, ncrit, a, d;

    unsigned *dmap, *x, *p;

#if 0  
    long long int visited = 0;
#endif
    
    const int cols = (nvars - 1) / BPU + 1;

    /* assume two samples of equal size */
    if (nvars % 2) {
      fprintf(stderr,"ERROR: Sample size must be positive and even\n");
      fprintf(stderr,"       (assuming two independent samples of equal size)\n");
      return NULL;
    }

    /* number of permutations must be positive */
    if (nperms <= 0) {
      fprintf(stderr,"ERROR: Number of permutations must be positive\n");
      return NULL;      
    }

    /* Attainment data is stored in an array of bitmaps */
    dmap = calloc(npoints, cols * sizeof(unsigned));

    /* Temporary bitmap */
    x = calloc(cols, sizeof(unsigned));
    
    /* Absolute distance between EAFs per permutation */
    D = calloc(nperms, sizeof(*D));

    /* Null distribution histogram */
    tail = malloc(1+nvars/2 * sizeof(*tail));
    tail[0] = nperms;
    for (i = 1; i <= nvars/2; tail[i++] = 0);
    critv = 0;
    ncrit = 0;
    
    /* Convert significance level to no. of permutations */
    a = alpha * nperms;

    /* Create random mask */
    p = randmask(nperms, nvars, nvars/2);

    /* Store data into dmap */
    for (i = 0; i < npoints; i++) {
	for (j = 0; j < cols; j++)
	    for (k = 0; k < BPU && (l = j * BPU + k) < nvars; k++)
		dmap[i * cols + j] |= (data[i * nvars + l]!=0) << k;
    }

          for (i = 0; i < npoints; i++) {
		    w = 0;
		    for (k = 0; k < cols; k++) {
			x[k] = dmap[i * cols + k];
			w += bit_weight(x[k]);
		    }
		    if (w <= critv || (nvars - w) <= critv)
			continue;
#if 0
		    visited++;
#endif
		    for (n = 0; n < nperms; n++)
			if (w > D[n] && (nvars - w) > D[n]) {
			    d = 0;
			    for (k = 0; k < cols; k++)
				    d += bit_weight(x[k] & p[n*cols+k]) - bit_weight(x[k] & ~p[n*cols+k]);
			    if (D[n] < abs(d)) {
			        tail[D[n]]--;
				tail[abs(d)]++;
				if (D[n] <= critv && abs(d) > critv) {
				  ncrit++;
				  while (ncrit > a)
				    ncrit -= tail[++critv];
#if 0
                                  fprintf(stderr, "critv = %d, ncrit = %d\n", critv, ncrit);
                                  fflush(stderr);
#endif
                                }
				D[n] = abs(d);
                            }
			}
          }
    free(dmap);
    free(p);
    free(x);
    free(D);
    
    tail[0] = critv;
    for (i = 1; i <= critv; tail[i++]=0);

    return tail;
}

int eaf2_ks_stat(int *data, int nvars, int npoints)
{
    int i, j, k, l, maxd=0, d, cols;
    const int block = 8192/2;

    unsigned *dmap, *c, *mask;

    cols = (nvars - 1) / BPU + 1;
    dmap = calloc(npoints, cols * sizeof(unsigned));
    c = calloc(cols, sizeof(unsigned));
    mask = calloc(cols, sizeof(unsigned));

    for (j = 0; j < cols; j++)
    	for (k = 0; k < BPU && (l = j * BPU + k) < nvars/2; k++)
	    mask[j] |= (1<<k);

#if 0
    printf("mask: %8x %8x\n",mask[1],mask[0]);
    printf("mask weight: %d %d\n", bit_weight(mask[1]), bit_weight(mask[0]));
#endif

    for (i = 0; i < npoints; i++) {
	for (j = 0; j < cols; j++)
	    for (k = 0; k < BPU && (l = j * BPU + k) < nvars; k++)
		dmap[i * cols + j] |= (data[i * nvars + l]!=0) << k;
/*	printf("data[%d]: %x %x\n",i, dmap[i * cols + 1], dmap[i * cols]); */
/*	printf("%d\n", bit_weight(dmap[i * cols + 1])+bit_weight(dmap[i * cols]));*/
    }

    for (l = 0; l < npoints; l += block) {
/*    printf("l = %d\n", l);*/
	for (i = l; i < npoints; i++) {
	    for (j = l; j < (l + block) && j <= i; j++) {
		d = 0;
		for (k = 0; k < cols; k++) {
		    c[k] = dmap[i * cols + k] & dmap[j * cols + k];
		    d += bit_weight(c[k] & mask[k]) - bit_weight(c[k] & ~mask[k]);
		}
		if (maxd < abs(d))
		    maxd = abs(d);
	    }
	}
    }
    free(dmap);
    free(c);
    free(mask);
    
    return maxd;
}


int *eaf2_ks_tail(int *data, int nvars, int npoints, double alpha, int nperms)
{
    int i, j, k, l;
    int n;
    int w, *D, *tail, critv, ncrit, a, d;

    unsigned *dmap, *x, *p;

#if 0  
    long long int visited = 0;
#endif
    
    const int cols = (nvars - 1) / BPU + 1;
    const int jblock = 4096	/* XBLOCK / (sizeof(unsigned) * cols)*/;

    /* assume two samples of equal size */
    if (nvars % 2) {
      fprintf(stderr,"ERROR: Sample size must be positive and even\n");
      fprintf(stderr,"       (assuming two independent samples of equal size)\n");
      return NULL;
    }

    /* number of permutations must be positive */
    if (nperms <= 0) {
      fprintf(stderr,"ERROR: Number of permutations must be positive\n");
      return NULL;      
    }

    /* Attainment data is stored in an array of bitmaps */
    dmap = calloc(npoints, cols * sizeof(unsigned));

    /* Temporary bitmap */
    x = calloc(cols, sizeof(unsigned));
    
    /* Absolute distance between EAFs per permutation */
    D = calloc(nperms, sizeof(*D));

    /* Null distribution histogram */
    tail = malloc(1+nvars/2 * sizeof(*tail));
    tail[0] = nperms;
    for (i = 1; i <= nvars/2; tail[i++] = 0);
    critv = 0;
    ncrit = 0;
    
    /* Convert significance level to no. of permutations */
    a = alpha * nperms;

    /* Create random mask */
    p = randmask(nperms, nvars, nvars/2);

    /* Store data into dmap */
    for (i = 0; i < npoints; i++) {
	for (j = 0; j < cols; j++)
	    for (k = 0; k < BPU && (l = j * BPU + k) < nvars; k++)
		dmap[i * cols + j] |= (data[i * nvars + l]!=0) << k;
    }

    for (l = 0; l < npoints; l += jblock) {
          for (i = l; i < npoints; i++) {
		for (j = l; j < (l + jblock) && j <= i; j++) {
		    w = 0;
		    for (k = 0; k < cols; k++) {
			x[k] = (dmap[i * cols + k] & dmap[j * cols + k]);
			w += bit_weight(x[k]);
		    }
		    if (w <= critv || (nvars - w) <= critv)
			continue;
#if 0
		    visited++;
#endif
		    for (n = 0; n < nperms; n++)
			if (w > D[n] && (nvars - w) > D[n]) {
			    d = 0;
			    for (k = 0; k < cols; k++)
				    d += bit_weight(x[k] & p[n*cols+k]) - bit_weight(x[k] & ~p[n*cols+k]);
			    if (D[n] < abs(d)) {
			        tail[D[n]]--;
				tail[abs(d)]++;
				if (D[n] <= critv && abs(d) > critv) {
				  ncrit++;
				  while (ncrit > a)
				    ncrit -= tail[++critv];
#if 0
                                  fprintf(stderr, "critv = %d, ncrit = %d\n", critv, ncrit);
                                  fflush(stderr);
#endif
                                }
				D[n] = abs(d);
                            }
			}
		}
          }
    }
    free(dmap);
    free(p);
    free(x);
    free(D);
    
    tail[0] = critv;
    for (i = 1; i <= critv; tail[i++]=0);

    return tail;
}


