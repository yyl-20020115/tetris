package com.noc.tet;

import java.util.Random;

public class PieceGenerator {

	public static final int START_RANDOM = 0;
	public static final int START_7BAG = 1;
	
	int strategy;
	int[] bag;
	int bagPointer;
	private final Random random;
	
	public PieceGenerator(int start) {
		bag = new int[7];
		for(int i = 0; i < 7; i++) //initial Permutation
			bag[i] = i;
		
		random = new Random(System.currentTimeMillis());
        this.strategy = start == START_RANDOM ? START_RANDOM : START_7BAG;
		
		// Fill initial Bag
		for(int i = 0; i < 6; i++) {
			int c = random.nextInt(7-i);
			int t = bag[i]; bag[i] = bag[i+c]; bag[i+c] = t;	/* swap */
		}
		bagPointer = 0;
	}

	public int next() {
		if(strategy== START_RANDOM)
			return random.nextInt(7);
		else {
			if(bagPointer < 7) {
				bagPointer++;
            } else {
				// Randomize Bag
				for(int i = 0; i < 6; i++) {
					int c = random.nextInt(7-i);
					int t = bag[i]; bag[i] = bag[i+c]; bag[i+c] = t;	/* swap */
				}
				bagPointer = 1;
            }
            return bag[bagPointer - 1];
        }
	}
}