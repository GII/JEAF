/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mpi.MPI;

/**
 * Random number generator.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EAFRandom {

    /** Random seed. */
    private static long _seed = 0;
    
    /** Random generator. */
    private static Random rand = null;

    /** States if this class is initialized */
    private static boolean initialized = false;
    
    /**
     * Initializes this random number generator. The seed is taken to be the 
     * difference, measured in milliseconds, between the current time and 
     * midnight, January 1, 1970 UTC.<p>
     * 
     * This method must be called before generating any random number.
     */
    public static void init() {
        init(System.currentTimeMillis());        
    }

    /**
     * Initializes this random number generator.<p>
     * 
     * This method must be called before generating any random number.
     * 
     * @param seed The seed for the random number generator.
     */
    public static void init(long seed) {
        if (MPI.Initialized()) { /* Parallel environment */
            if (MPI.COMM_WORLD.Size() == 1) { /* only one node */
                _seed = seed;
                rand = new Random(getSeed());
            } else { /* several nodes */

                /* Buffer for sending the seeds */
                long[] seeds = new long[MPI.COMM_WORLD.Size()];
                
                /* Buffer for receiving the seed */
                long[] recvBuffer = new long[1];
                
                if (MPI.COMM_WORLD.Rank() == 0) { /* node that generates seeds */
                    
                    /* save state for this node */
                    _seed = seed;
                    rand = new Random(getSeed());
                   
                    seeds[0] = getSeed(); /* this nodes's seed */
                                        
                    /* Generator for generating the other seeds. Don't use the
                     * generator of this node (variable rand) since this changes its state. */
                    Random seedGenerator = new Random(getSeed());
                    
                    List<Long> nseeds = new ArrayList<Long>();
                    nseeds.add(new Long(seed));
                    for (int n = 1; n < seeds.length; n++) {                        
                        Long newSeed = new Long(seedGenerator.nextLong());
                        while(nseeds.contains(newSeed)) {
                            newSeed = new Long(seedGenerator.nextLong());
                        }                        
                        seeds[n] = newSeed.longValue();
                        nseeds.add(newSeed);
                    }
                }
                
                /* Scatter seeds */
                MPI.COMM_WORLD.Scatter(seeds, 0, recvBuffer.length, MPI.LONG, 
                        recvBuffer, 0, recvBuffer.length, MPI.LONG, 0);
                
                /* Since the node 0 has already his seed, it needs not to create
                 * another generator. The other nodes create their generator here. */
                if (MPI.COMM_WORLD.Rank() != 0) {
                    _seed = recvBuffer[0];
                    rand = new Random(getSeed());
                }
            }
        } else { /* Assume no parallel environment present */
            _seed = seed;
            rand = new Random(getSeed());
        }
        
        initialized = true;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from 
     * this random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed boolean value from 
     * this random number generator's sequence.
     * @throws NullPointerException if this class is not initialized.
     */
    public static boolean nextBoolean() {
        return rand.nextBoolean();
    }
    
    /**
     * Generates random bytes and places them into a user-supplied byte array. 
     * The number of random bytes produced is equal to the length of the byte array.
     * 
     * @param bytes The byte array to fill with random bytes. 
     * @throws NullPointerException If this class is not initialized or if the 
     * byte array is null.
     */
    public static void nextBytes(byte[] bytes) {
        rand.nextBytes(bytes);
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed double value 
     * between 0.0 and 1.0 from this random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed double value 
     * between 0.0 and 1.0 from this random number generator's sequence
     * 
     * @throws NullPointerException if this class is not initialized.
     */
    public static double nextDouble() {
        return rand.nextDouble();
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from this random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed float value
     * between 0.0 and 1.0 from this random number generator's sequence.
     * 
     * @throws NullPointerException if this class is not initialized.
     */
    public static float nextFloat() {
        return rand.nextFloat();
    }
    
    /**
     * Returns the next pseudorandom, Gaussian ("normally") distributed double
     * value with mean 0.0 and standard deviation 1.0 from this random number
     * generator's sequence.
     * 
     * @return The next pseudorandom, Gaussian ("normally") distributed double
     * value with mean 0.0 and standard deviation 1.0
     * 
     * @throws NullPointerException if this class is not initialized.
     */
    public static double nextGaussian() {
        return rand.nextGaussian();
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed int value from this
     * random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed int value from this
     * random number generator's sequence.
     * 
     * @throws NullPointerException if this class is not initialized.
     */
    public static int nextInt() {
        return rand.nextInt();
    }
    
    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 
     * (inclusive) and the specified value (exclusive), drawn from this random
     * number generator's sequence.
     * 
     * @param n The bound on the random number to be returned. Must be positive.
     * 
     * @return The next pseudorandom, uniformly distributed int value between 0
     * (inclusive) and n (exclusive) from this random number generator's sequence.
     * 
     * @throws NullPointerException if this class is not initialized.
     * @throws IllegalArgumentException if n is not positive.
     */
    public static int nextInt(int n) {
        return rand.nextInt(n);
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed long value from this
     * random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed long value from this
     * random number generator's sequence
     * 
     * @throws NullPointerException if this class is not initialized.
     */
    public static long nextLong() {
        return rand.nextLong();
    }
    
    /**
     * Test for initialization.
     * 
     * @return <code>true</code> if the generator is initialized. <code>false</code>
     * otherwise.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * The seed currently used by the random number generator.
     * 
     * @return The seed currently used by the random number generator.
     */
    public static long getSeed() {
        return _seed;
    }
    
    /**
     * Class constructor: Singleton class.
     */
    private EAFRandom() {
    }
}
