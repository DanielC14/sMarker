package com.smarker.impl;

import static com.mongodb.client.model.Filters.eq;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.mongodb.Block;
import com.mongodb.client.result.DeleteResult;
import com.smarker.init.JWT;
import com.smarker.init.MongoDB;
import com.smarker.model.Comment;

public class CommentManager implements IComment{

	static CommentManager cm = null;

	public static CommentManager getInstance() {
		if (cm == null) {
			cm = new CommentManager();
		}
		return cm;
	}

	// GET ALL COMMENTS
	public List<Comment> getComments() {
		List<Comment> comments = new ArrayList<Comment>();

		Block<Comment> printComments = new Block<Comment>() {
			public void apply(final Comment cm) {
				comments.add(cm);
			}
		};

		MongoDB.CommentCollection.find().forEach(printComments);
		return comments;
	}

	// GET COMMENTS BY ID OR MADE BY A USER OR IN A BOOKMARK
	public List<Comment> getCommentsByField(String id, String field) {
		List<Comment> comments = new ArrayList<Comment>();

		Block<Comment> printComments = new Block<Comment>() {
			public void apply(final Comment cm) {
				comments.add(cm);
			}
		};

		MongoDB.CommentCollection.find(eq(field, id)).forEach(printComments);
		return comments;

	}

	// CREATE COMMENT
	public Response createComment(String json) {
		
		JSONObject obj = new JSONObject(json);
		String comment = obj.getString("message");
		String bookmarkId = obj.getString("bookmarkId");
		String token = obj.getString("token");
		
		String userId = JWT.ReverseToken(token, "userId");
		String username = JWT.ReverseToken(token, "username");

		Comment cm = new Comment(comment, username, userId, bookmarkId);
		MongoDB.CommentCollection.insertOne(cm);

		return Response.ok().build();

	}

	// DELETE ALL COMMENTS BY ID, MADE BY A USER OR IN A BOOKMARK
	public String deleteComment(String id, String field) {

		DeleteResult result = MongoDB.CommentCollection.deleteMany(eq(field, id));

		String message = "";
		if (result.getDeletedCount() == 0) {
			message = "Comments not found!";
		} else {
			message = "Comments deleted!";
		}
		return message;

	}

}
