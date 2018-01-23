package com.smarker.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement(name = "comment")
public class Comment {
	
	private String id;
	private String message;
	private String userId;
	private String username;
	private String bookmarkId;
	private String link;
	
	public Comment() {
	}

	public Comment(String message, String username, String userId, String bookmarkId) {
		this.id = new ObjectId().toString();
		this.message = message;
		this.username = username;
		this.userId = userId;
		this.bookmarkId = bookmarkId;
		this.link = "comment/" + this.id;
	}
	
	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement
	public String getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(String bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	@XmlElement
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
