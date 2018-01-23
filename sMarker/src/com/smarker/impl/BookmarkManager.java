package com.smarker.impl;

import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.smarker.init.MongoDB;
import com.smarker.model.Bookmark;
import com.smarker.model.Comment;

public class BookmarkManager implements IBookmark {

	static BookmarkManager bm = null;

	public static BookmarkManager getInstance() {
		if (bm == null) {
			bm = new BookmarkManager();
		}
		return bm;
	}

	// GET ALL BOOKMARKS, returns them sorted alphabetically or most used
	public List<Bookmark> getBookmarks(String sort) {

		List<Bookmark> bms = new ArrayList<Bookmark>();

		Block<Bookmark> printBookmarks = new Block<Bookmark>() {
			public void apply(final Bookmark bm) {
				bms.add(bm);
			}
		};
		try
		{
			if(sort.equals("count"))
			{
				MongoDB.BookMarkCollection.find().sort(Sorts.descending("counter")).forEach(printBookmarks);
			}
			else
			{
				MongoDB.BookMarkCollection.find().sort(Sorts.ascending("name")).forEach(printBookmarks);
			}
		}
		catch(NullPointerException e) {
			MongoDB.BookMarkCollection.find().sort(Sorts.ascending("name")).forEach(printBookmarks);
		}
		
		
		return bms;
	}

	// GET BOOKMARK BY FIELDS
	public List<Bookmark> getBookmarksByFields(String category, String tags, String name, String id) {

		BasicDBObject criteria = new BasicDBObject();
		
		String[] temp_tags;
		
		if (category != null && category != "")
			criteria.append("category", category);

		if (tags != null)
		{
			temp_tags = tags.split(",");
			System.out.println(temp_tags[0]);
			criteria.append("tags", (tags));
		}
			

		if (name != null)
			criteria.append("name", name);

		if (id != null)
			criteria.append("id", id);

		List<Bookmark> bms = new ArrayList<Bookmark>();

		Block<Bookmark> printBookmarks = new Block<Bookmark>() {
			public void apply(final Bookmark bm) {
				bms.add(bm);
			}
		};
		System.out.println(criteria);
		MongoDB.BookMarkCollection.find(criteria).forEach(printBookmarks);
		System.out.println(bms);
		return bms;

	}

	// ADDS A BOOKMARK TO DB
	@SuppressWarnings("incomplete-switch")
	public Bookmark createBookmark(String json) {

		
		
		JSONObject new_bookmark = new JSONObject(json);
		String name = new_bookmark.getString("name");
		String url = new_bookmark.getString("url");
		String category = new_bookmark.getString("category");
		JSONArray temp_tags = new_bookmark.getJSONArray("tags");
		
		List<String> tags = new ArrayList<String>();
		
		for(int i = 0; i<temp_tags.length();i++)
		{
			tags.add(temp_tags.get(i).toString());
		}

		if (MongoDB.BookMarkCollection.count(eq("url", url)) <= 0) {

			//HTTPConnection to get the icon
			StringBuilder result = new StringBuilder();
			URL url2;
			String icon_url = null;
			try {
				url2 = new URL("https://icons.better-idea.org/allicons.json?url=" + url);
				HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
				conn.setRequestMethod("GET");
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				rd.close();

				JSONObject obj = new JSONObject(result.toString());
				JSONArray icons = obj.getJSONArray("icons");

				icon_url = icons.getJSONObject(0).getString("url");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Bookmark b = new Bookmark(name, url, 0, category, icon_url, tags);
			MongoDB.BookMarkCollection.insertOne(b);
			
			return b;
		}
		else
		{
			return MongoDB.BookMarkCollection.find(eq("url", url)).first();
		}
		

	}

	// DELETES A BOOKMARK FROM DB
	public String deleteBookmark(String id) {

		DeleteResult result = MongoDB.BookMarkCollection.deleteOne(eq("_id", id));

		String message = "";
		if (result.getDeletedCount() == 0) {
			message = "Bookmark not found!";
		} else {
			message = "Bookmark deleted!";
		}
		return message;
	}
	
	//Gets all the comments made in a bookmark
	public List<Comment> getCommentsOfBookmark(String bookmarkId)
	{
		List<Comment> cms = new ArrayList<Comment>();

		Block<Comment> printComments = new Block<Comment>() {
			public void apply(final Comment cm) {
				cms.add(cm);
			}
		};
		MongoDB.CommentCollection.find(eq("bookmarkId", bookmarkId)).forEach(printComments);
		return cms;
	}

}
