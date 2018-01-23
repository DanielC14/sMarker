package com.smarker.api;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.smarker.impl.CommentManager;
import com.smarker.init.JWT;
import com.smarker.init.MongoDB;
import com.smarker.model.Comment;
import com.smarker.model.ErrorSM;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Path("/comments")
public class CommentResource {

	CommentManager cm = CommentManager.getInstance();

	// GET all comments, returns JSON
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getComments() {

		return cm.getComments();
	}

	// GET all comments, returns XML
	@GET
	@Path("/xml")
	@Produces(MediaType.APPLICATION_XML)
	public List<Comment> getCommentsXML() {

		return cm.getComments();
	}

	//GET comments by field, it can be userID, bookmarkID or commentID, returns JSON
	@Path("/{field}/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsByField(@PathParam("id") String id, @PathParam("field") String field) {

		return cm.getCommentsByField(id, field);

	}
	
	//GET comments by field, it can be userID, bookmarkID or commentID, returns XML
	@Path("/{field}/{id}/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Comment> getCommentsByFieldXML(@PathParam("id") String id, @PathParam("field") String field) {

		return cm.getCommentsByField(id, field);

	}

	//POST a comment, the user must be logged in to post a comment
	@POST
	@Consumes("application/json")
	public Response createComment(String json) {

		String token = new JSONObject(json).getString("token");

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);

			return cm.createComment(json);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	//DELETE a/all comment/s by field, it can be bookmarkID, userID or just the commentID
	@Path("/{field}/{id}")
	@DELETE
	public Response deleteComment(@PathParam("id") String id, @PathParam("field") String field) {
		String message = cm.deleteComment(id, field);

		return Response.ok().entity(message).build();
	}

}
