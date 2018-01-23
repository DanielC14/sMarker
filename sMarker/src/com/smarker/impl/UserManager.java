package com.smarker.impl;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.StringReader;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.smarker.init.JWT;
import com.smarker.init.MongoDB;
import com.smarker.model.Bookmark;
import com.smarker.model.ErrorSM;
import com.smarker.model.User;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import io.jsonwebtoken.Jwts;

public class UserManager implements IUser {

	static UserManager um = null;

	public static UserManager getInstance() {
		if (um == null) {
			um = new UserManager();
		}
		return um;
	}

	// GET ALL USERS
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		Block<User> printUsers = new Block<User>() {
			public void apply(final User user) {
				users.add(user);
			}
		};

		MongoDB.UserCollection.find().forEach(printUsers);
		return users;
	}

	// GET USER BY ID
	public User getUserById(String id) {
		User user = MongoDB.UserCollection.find(eq("_id", id)).first();
		return user;

	}

	// CREATE USER
	public Response createUser(String email, String name, String password) {

		if (MongoDB.UserCollection.find(eq("email", email)).first() == null) {
			User temp_u = new User(name, password, email);
			MongoDB.UserCollection.insertOne(temp_u);
			User u = MongoDB.UserCollection.find(eq("email", email)).first();
			Map<String, Object> user = new HashMap<String, Object>();
			user.put("userId", u.getId());
			user.put("username", u.getName());
			String token = JWT.GenerateJWT(user);
			return Response.ok().entity(token).build();
		} else {
			ErrorSM e = MongoDB.ErrorCollection.find(eq("code", "1001")).first();
			return Response.ok().entity(e).build();
		}
	}

	// DELETE USER
	public String deleteUser(String id) {
		DeleteResult result = MongoDB.UserCollection.deleteOne(eq("_id", id));

		String message = "";
		if (result.getDeletedCount() == 0) {
			message = "User not found!";
		} else {
			message = "User deleted!";
		}
		return message;
	}

	// UPDATE USER
	public Response updateUser(String token, String section, String json) {
		
		String userId = JWT.ReverseToken(token, "userId");

		JSONObject json_data = new JSONObject(json);

		String new_data = json_data.getString("new_data");
		
		String old_data;
		
		if(section.equals("password"))
		{
			old_data = json_data.getString("old_data");
			User u = MongoDB.UserCollection.find(eq("_id", userId)).first();
			if(!u.getPassword().equals(old_data))
			{
				ErrorSM e = MongoDB.ErrorCollection.find(eq("code","1106")).first();
				return Response.ok().entity(e).build();
			}
		}
		
		if(section.equals("email"))
		{
			User u = MongoDB.UserCollection.find(eq("email", new_data)).first();
			if(u != null)
			{
				ErrorSM e = MongoDB.ErrorCollection.find(eq("code","1001")).first();
				return Response.ok().entity(e).build();
			}
		}

		MongoDB.UserCollection.updateOne(eq("_id", userId), set(section, new_data));
		
		ErrorSM e = null;
		
		if(section.equals("name"))
		{
			e = MongoDB.ErrorCollection.find(eq("code","1104")).first();
		}
		else if(section.equals("email"))
		{
			e = MongoDB.ErrorCollection.find(eq("code","1103")).first();
		}
		else if(section.equals("password"))
		{
			e = MongoDB.ErrorCollection.find(eq("code","1105")).first();
		}
		
		
		
		return Response.ok().entity(e).build();

	}

	// GET BOOKMARKS OF AN USER
	public Response getUserBookmarks(String token, String sort) {

		String id = JWT.ReverseToken(token, "userId");

		User u = MongoDB.UserCollection.find(eq("_id", id)).first();
		List<Bookmark> bms = u.getBookmarks();
		
		try {
			if (sort.equals("count")) {
				bms.sort(new Comparator<Bookmark>() {
					public int compare(final Bookmark bm1, final Bookmark bm2) {
						return bm2.getCounter() - bm1.getCounter();
					}
				});
			}
			else
			{
				bms.sort(new Comparator<Bookmark>() {
					public int compare(final Bookmark bm1, final Bookmark bm2) {
						return bm1.getName().compareTo(bm2.getName());
					}
				});
			}
		}catch(NullPointerException e) {
			
			bms.sort(new Comparator<Bookmark>() {
				public int compare(final Bookmark bm1, final Bookmark bm2) {
					return bm1.getName().compareTo(bm2.getName());
				}
			});
			
		}
		
		

		return Response.ok().entity(bms).build();
	}

	// ADD A BOOKMARK TO AN USER
	public Response addNewBookmarkToUser(String token, String json) {
		String userId = JWT.ReverseToken(token, "userId");

		JSONObject bookmark = new JSONObject(json);
		String name = bookmark.getString("name");
		String url = bookmark.getString("url");
		String color = bookmark.getString("color");

		BookmarkManager bma = BookmarkManager.getInstance();
		Bookmark bm = bma.createBookmark(json);

		MongoDB.BookMarkCollection.updateOne(eq("_id", bm.getId()), set("counter", bm.getCounter() + 1));

		User u = MongoDB.UserCollection.find(eq("_id", userId)).first();

		Bookmark bookmarkToUser = new Bookmark(bm.getId(), name, url, bm.getIcon_url(), color);

		List<Bookmark> bms = u.getBookmarks();
		bms.add(bookmarkToUser);
		MongoDB.UserCollection.updateOne(eq("_id", userId), set("bookmarks", bms));

		return Response.ok().build();

	}
	
	//Adds an existing bookmark to an user
	public Response addExistingBookmarkToUser(String token, String bookmarkId, String json) {

		JSONObject bookmark = new JSONObject(json);
		String name = bookmark.getString("name");
		String color = bookmark.getString("color");

		String userId = JWT.ReverseToken(token, "userId");
		
		Bookmark bm = MongoDB.BookMarkCollection.find(eq("_id", bookmarkId)).first();
		Bookmark bookmarkToUser = new Bookmark(bm.getId(), name, bm.getUrl(), bm.getIcon_url(), color);

		User u = MongoDB.UserCollection.find(eq("_id", userId)).first();
		List<Bookmark> bms = u.getBookmarks();
		bms.add(bookmarkToUser);
		MongoDB.UserCollection.updateOne(eq("_id", userId), set("bookmarks", bms));

		return Response.ok().build();

	}

	//updates the counter of a bookmark of an user
	public Response updateBookmarkOfUser(String token, String bookmarkId) {
		String userId = JWT.ReverseToken(token, "userId");

		User u = MongoDB.UserCollection.find(eq("_id", userId)).first();

		List<Bookmark> bms = u.getBookmarks();

		for (Bookmark bookmark : bms) {
			if (bookmark.getId().equals(bookmarkId)) {
				bookmark.setCounter(bookmark.getCounter() + 1);
			}
		}

		MongoDB.UserCollection.updateOne(eq("_id", userId), set("bookmarks", bms));

		return Response.ok().build();

	}

	//Deletes a bookmark of an user
	public Response deleteBookmarksOfUser(String token, String json) {
		String userId = JWT.ReverseToken(token, "userId");

		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("bookmarks");

		User u = MongoDB.UserCollection.find(eq("_id", userId)).first();

		List<Bookmark> bms = u.getBookmarks();

		for (int i = 0; i < array.length(); i++) {
			for (Iterator<Bookmark> iterator = bms.iterator(); iterator.hasNext();) {
				Bookmark bm = iterator.next();
				if (bm.getId().equals(array.getString(i))) {
					iterator.remove();
				}
			}
		}

		MongoDB.UserCollection.updateOne(eq("_id", userId), set("bookmarks", bms));

		return Response.ok().build();
	}

	//executes login of an user, returns its token
	public Response login(String email, String password) {

		BasicDBObject criteria = new BasicDBObject();
		criteria.append("email", email);
		criteria.append("password", password);

		User u = MongoDB.UserCollection.find(criteria).first();

		if (u == null) {
			ErrorSM e = MongoDB.ErrorCollection.find(eq("code", "1002")).first();
			return Response.serverError().status(e.getHttpStatus()).entity(e).build();
		} else {

			Map<String, Object> user = new HashMap<String, Object>();
			user.put("userId", u.getId());
			user.put("username", u.getName());

			String token = JWT.GenerateJWT(user);
			return Response.ok().entity(token).build();

		}

	}

	//executes the register of an user, returns its token
//	public Response register(String email, String name, String password) {
//		if (MongoDB.UserCollection.find(eq("email", email)).first() == null) {
//			User temp_u = new User(name, password, email);
//			MongoDB.UserCollection.insertOne(temp_u);
//			User u = MongoDB.UserCollection.find(eq("email", email)).first();
//			Map<String, Object> user = new HashMap<String, Object>();
//			user.put("userId", u.getId());
//			user.put("username", u.getName());
//			String token = JWT.GenerateJWT(user);
//			return Response.ok().entity(token).build();
//		} else {
//			ErrorSM e = MongoDB.ErrorCollection.find(eq("code", "1001")).first();
//			return Response.ok().entity(e).build();
//		}
//
//	}

}
