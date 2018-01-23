package com.smarker.impl;

import java.util.List;

import com.smarker.model.Bookmark;
import com.smarker.model.Comment;

public interface IBookmark {
	
	public List<Bookmark> getBookmarks(String sort);
	public List<Bookmark> getBookmarksByFields(String category, String tags, String name, String id);
	public String deleteBookmark(String id);
	public Bookmark createBookmark(String json);
	public List<Comment> getCommentsOfBookmark(String bookmarkId);
	
}
