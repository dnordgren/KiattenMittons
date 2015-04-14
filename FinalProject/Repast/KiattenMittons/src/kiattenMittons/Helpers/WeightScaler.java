package kiattenMittons.Helpers;

public class WeightScaler {

	/**
	 * 
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
		
		double[] output = new double[size];
		for(int i = 0; i < size; i++) {
			output[i] = original[i] * multiplier;
		}
		
		return output;
	}
}
