package com.dream.core.output;

/**
 * @author Alessandro Maggi
 *
 */
public class DummyOutput implements Output {

	public DummyOutput() {}

	public void write(Writable out) {}

	@Override
	public void write(String topic, Writable out) {}

}
