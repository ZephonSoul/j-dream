package com.dream.core.output;

/**
 * @author Alessandro Maggi
 *
 */
public interface Output {

	public void write(Writable out);
	
	public void write(String topic, Writable out);
	
}
