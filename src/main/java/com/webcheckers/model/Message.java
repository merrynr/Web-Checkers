package com.webcheckers.model;

import java.io.Serializable;

public class Message implements Serializable {
	public enum TYPE {info, ERROR}
 
	public String text;
	public TYPE type;

	public Message(TYPE type, String text) {
		this.text = text;
		this.type = type;
	}
}
