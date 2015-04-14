package kiattenMittons.Helpers;

import java.util.Random;

public class WeightedProbability {
	
	private static Random randomGenerator = new Random();
	
	/**
	 * Given a set of weights for indices, return a random
	 * index according to those weights
	 * @param weights
	 * @return
	 */
	public static int weightedSelect(double[] weights) {
		double generated = randomGenerator.nextDouble();
		
		double sum = 0;
		for(int i = 0; i < weights.length; i++) {
			sum += weights[i];
			if(sum > generated) {
				return i;
			}
		}
		//if nothing was matched, it's the last item
		return weights.length - 1;
	}
}
