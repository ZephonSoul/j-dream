package com.dream.core.output;

/**
 * @author Alessandro Maggi
 *
 */
public class ConsoleOutput implements Output {

	public ConsoleOutput() {}

	public void write(Writable out) {
		System.out.println("\n" + out.getString());
	}

}
