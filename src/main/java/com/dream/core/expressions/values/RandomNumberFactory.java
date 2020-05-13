/**
 * 
 */
package com.dream.core.expressions.values;

import java.util.Random;

/**
 * @author Alessandro Maggi
 *
 */
public class RandomNumberFactory {

	private static RandomNumberFactory factoryInstance;
	private Random generator;
	
	public RandomNumberFactory() {
		generator = new Random();
	}
	
	public static RandomNumberFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new RandomNumberFactory();
		return factoryInstance;
	}
	
	public double getRandomDouble(double max) {
		return generator.nextDouble()*max;
	}
	
	public double getRandomDouble() {
		return getRandomDouble(1);
	}

}
