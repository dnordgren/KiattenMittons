package kiattenMittons.Helpers;

public class WeightScaler {

	/**
	 * Takes an array of weights (should sum to 1) and extracts
	 * a subarray, scaling the subarray up to also sum to 1
	 * @param original weights array
	 * @param size of output array
	 * @return subset of weights array, scaled to add to one
	 */
	public static double[] scaleWeights(double[] original, int size) {
		
		if(size >= original.length) {
			return original;
		}
		
		double sum = 0;
		for(Double d : original) {
			sum += d;
		}
		
		double partialSum = 0;
		for(int i = 0; i < size; i++) {
			partialSum += original[i];
		}
		
		double multiplier = partialSum / sum;
		
		/*
		 * Multiply each element by a proportion to get its
		 * size in the scaled subarray
		 */
		double[] output = new double[size];
		for(int i = 0; i < size; i++) {
			output[i] = original[i] * multiplier;
		}
		
		return output;
	}
}
