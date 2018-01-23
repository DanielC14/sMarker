package com.smarker.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smarker.impl.BookmarkManager;
import com.smarker.model.Bookmark;
import com.smarker.model.Comment;

@Path("/bookmarks")
public class BookmarkResource {

	BookmarkManager bm = BookmarkManager.getInstance();

	// GET all the bookmarks, returns JSON
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bookmark> getBookmarks(@QueryParam("sort") String sort) {
		return bm.getBookmarks(sort);
	}

	// GET all the bookmarks, returns XML
	@Path("/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Bookmark> getBookmarksXML(@QueryParam("sort") String sort) {
		return bm.getBookmarks(sort);
	}

	// GET bookmarks by filters, the filters are category, tag, name and bookmarkID, returns JSON
	@Path("/filter")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bookmark> getBookmarksByFields(@QueryParam("category") String category, @QueryParam("tags") String tags,
			@QueryParam("name") String name, @PathParam("bookmarkId") String id) {
		System.out.println(tags);
		return bm.getBookmarksByFields(category, tags, name, id);
	}

	// GET bookmarks by filters, the filters are category, tag, name and bookmarkID, returns XML
	@Path("/filter/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Bookmark> getBookmarksByFieldsXML(@QueryParam("category") String category,
			@QueryParam("tags") String tags, @QueryParam("name") String name, @PathParam("bookmarkId") String id) {

		return bm.getBookmarksByFields(category, tags, name, id);
	}

	// Post a new bookmark
	@POST
	@Consumes("application/json")
	public Response insertBookmark(String json) {

		bm.createBookmark(json);

		return Response.ok().entity("Bookmark created!").build();
	}

	// DELETE a specific bookmark from the DB
	@Path("/{bookmarkId}")
	@DELETE
	public Response removeBookmark(@PathParam("bookmarkId") String id) {
		String message = bm.deleteBookmark(id);
		return Response.ok().entity(message).build();
	}

	//GET comments of a specific bookmark, returns JSON
	@Path("/{bookmarkId}/comments")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsOfBookmark(@PathParam("bookmarkId") String bookmarkId) {
		return bm.getCommentsOfBookmark(bookmarkId);
	}
	
	//GET comments of a specific bookmark, returns XML
	@Path("/{bookmarkId}/comments/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Comment> getCommentsOfBookmarkXML(@PathParam("bookmarkId") String bookmarkId) {
		return bm.getCommentsOfBookmark(bookmarkId);
	}

}