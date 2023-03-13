package com.example.library.entities;

public class Message {

	public Message() {
		
	}

	private String text;
	private String type;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Message(String text, String type) {
		super();
		this.text = text;
		this.type = type;
	}
	
}
