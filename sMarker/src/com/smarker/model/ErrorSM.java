package com.smarker.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class ErrorSM {
	
	private String code;
	private String message;
	private int httpStatus;
	private String link;
	
	public ErrorSM() {}
	
	public ErrorSM(String code, String message, int httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
		this.link = "/message/" + code;
	}
	
	@XmlAttribute
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlElement
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlElement
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	@XmlElement	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

}
