package com.smarker.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import com.smarker.model.Bookmark;
import com.smarker.model.User;

public interface IUser {
	
	public List<User> getUsers();
	public User getUserById(String id);
	public Response createUser(String email, String name, String password);
	public String deleteUser(String id);
	public Response updateUser(String id, String section, String json);
	public Response getUserBookmarks(String id, String sort);
	public Response addNewBookmarkToUser(String userId, String json);
	public Response addExistingBookmarkToUser(String userId, String bookmarkId, String json);
	public Response deleteBookmarksOfUser(String token, String json);
	public Response login(String email, String password);
	public Response updateBookmarkOfUser(String token, String bookmarkId);

}
