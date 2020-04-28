package com.dream.core.output;

/**
 * @author Alessandro Maggi
 *
 */
public class MessageWritable implements Writable {
	
	private static MessageWritable instance;	
	private String message;
	
	private static MessageWritable getInstance() {
		if (instance == null)
			instance = new MessageWritable();
		return instance;
	}
	
	private void setMessage(String message) {
		this.message = message;
	}
	
	public static <T> Writable write(String preamble,T subject,String postscript) {
		MessageWritable writable = MessageWritable.getInstance();
		writable.setMessage(preamble + subject.toString() + postscript);
		return writable;
	}
	
	public static <T> Writable write(String preamble,T subject) {
		return write(preamble,subject,"");
	}
	
	public static <T> Writable write(T subject,String postscript) {
		return write("",subject,postscript);
	}
	
	public static <T> Writable write(T subject) {
		return write("",subject,"");
	}

	@Override
	public String getString() {
		return message;
	}

}
