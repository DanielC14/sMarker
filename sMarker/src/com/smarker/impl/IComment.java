package com.smarker.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import com.smarker.model.Comment;

public interface IComment {
	
	public List<Comment> getComments();
	public List<Comment> getCommentsByField(String id, String field);
	public Response createComment(String json);
	public String deleteComment(String id, String field);
	
}
