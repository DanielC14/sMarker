package com.smarker.api;

import static com.mongodb.client.model.Filters.eq;

import java.io.StringReader;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smarker.impl.BookmarkManager;
import com.smarker.impl.UserManager;
import com.smarker.init.JWT;
import com.smarker.init.MongoDB;
import com.smarker.model.Bookmark;
import com.smarker.model.ErrorSM;
import com.smarker.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Path("/users")
public class UserResource {

	UserManager um = UserManager.getInstance();

	// GET all the users, returns JSON
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {

		return um.getUsers();

	}

	// GET all the users, returns XML
	@Path("/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<User> getUsersXML() {

		return um.getUsers();

	}

	// GET user by id, returns JSON
	@Path("/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserById(@PathParam("userId") String id) {

		return um.getUserById(id);

	}

	// GET user by id, returns XML
	@Path("/{userId}/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public User getUserByIdXML(@PathParam("userId") String id) {

		return um.getUserById(id);

	}

	// Post a new user
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createUser(@FormParam("email") String email, @FormParam("password") String password,
			@FormParam("name") String name) {
		return um.createUser(email, name, password);

	}
	
	// DELETE a user by its id
	@Path("/{userId}")
	@DELETE
	public Response removeUser(@PathParam("userId") String id) {

		String message = um.deleteUser(id);

		return Response.ok().entity(message).build();
	}

	// UPDATE a specific user data, it can be the email, name or password
	@Path("/{token}")
	@PUT
	@Consumes("application/json")
	public Response updateUser(@PathParam("token") String token, @QueryParam("section") String section, String json) {

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.updateUser(token, section, json);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	//GET bookmarks of user, returns JSON
	@Path("/{token}/bookmarks")
	@GET
	@Produces("application/json")
	public Response getUserBookmarks(@PathParam("token") String token, @QueryParam("sort") String sort) {
		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.getUserBookmarks(token, sort);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}
	
	//GET bookmarks of user, returns XML
	@Path("/{token}/bookmarks/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getUserBookmarksXML(@PathParam("token") String token, @QueryParam("sort") String sort) {
		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.getUserBookmarks(token, sort);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	// Add a bookmark to an user, the bookmarks is created by the user
	@Path("/{token}/bookmarks")
	@POST
	@Consumes("application/json")
	public Response addNewBookmarkToUser(@PathParam("token") String token, String json) {

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.addNewBookmarkToUser(token, json);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	// Add an existing bookmark to an user
	@Path("/{token}/bookmarks/{bookmarkId}")
	@POST
	@Consumes("application/json")
	public Response addExistingBookmarkToUser(@PathParam("token") String token,
			@PathParam("bookmarkId") String bookmarkId, String json) {

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.addExistingBookmarkToUser(token, bookmarkId, json);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	// Update bookmark counter of user everytime he clicks on it
	@Path("/{token}/bookmarks/{bookmarkId}")
	@PUT
	@Consumes("application/json")
	public Response updateBookmarkOfUser(@PathParam("token") String token, @PathParam("bookmarkId") String bookmarkId) {

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.updateBookmarkOfUser(token, bookmarkId);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	// Delete bookmarks of user
	@Path("/{token}/bookmarks")
	@DELETE
	@Consumes("application/json")
	public Response deleteBookmarksOfUser(@PathParam("token") String token, String json) {

		try {

			Jwts.parser().setSigningKey(JWT.getKey()).parseClaimsJws(token);
			return um.deleteBookmarksOfUser(token, json);

		} catch (SignatureException e) {
			// don't trust the JWT!
			ErrorSM error = MongoDB.ErrorCollection.find(eq("code", "3001")).first();
			return Response.ok().entity(error).build();
		}

	}

	// Login user, returns the jwt
	@Path("/login")
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response userLogin(@FormParam("email") String email, @FormParam("password") String password) {

		return um.login(email, password);

	}

	// Register user, returns the jwt
//	@Path("/register")
//	@POST
//	@Consumes("application/x-www-form-urlencoded")
//	public Response userRegister(@FormParam("email") String email, @FormParam("password") String password,
//			@FormParam("name") String name) {
//		return um.createUser(email, name, password);
//
//	}

}
