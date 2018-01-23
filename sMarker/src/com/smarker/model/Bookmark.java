package com.smarker.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
public class Bookmark {

	private String id;
	private String name;
	private String url;
	private int counter;
	private String category;
	private String icon_url;
	private List<String> tags;
	private String color;
	private String link;

	public Bookmark() {

	}

	public Bookmark(String name, String url, int counter, String category, String icon_url, List<String> tags) {

		this.id = new ObjectId().toString();
		this.name = name;
		this.url = url;
		this.counter = counter;
		this.category = category;
		this.icon_url = icon_url;
		this.tags = tags;
		this.link = "bookmark/" + this.id;
	}

	public Bookmark(String id, String name, String url, String icon_url, String color) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.counter = 0;
		this.icon_url = icon_url;
		this.color = color;
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
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@XmlElement
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	@XmlElement
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	@XmlElement
	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	@XmlElement
	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	@XmlElement
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	@XmlElement
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
