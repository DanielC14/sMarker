package com.smarker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement(name = "user")
public class User {

	private String id;
	private String name;
	private String password;
	private String email;
	private List<Bookmark> bookmarks;
	private String link;
	
	public User() {
		
	}

	public User(String name, String password, String email) {
		this.id = new ObjectId().toString();
		this.name = name;
		this.password = password;
		this.email = email;
		this.bookmarks = new ArrayList<Bookmark>();
		this.link = "user/" + this.id;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	@XmlElement
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
