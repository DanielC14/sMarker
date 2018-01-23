package com.smarker.init;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.smarker.model.Bookmark;
import com.smarker.model.Comment;
import com.smarker.model.ErrorSM;
import com.smarker.model.User;

public class MongoDB {

	public static MongoDatabase db;
	
	public static MongoCollection<Bookmark> BookMarkCollection;
	public static MongoCollection<User> UserCollection;
	public static MongoCollection<Comment> CommentCollection;
	public static MongoCollection<ErrorSM> ErrorCollection;

	//connects the server to MongoDB
	@SuppressWarnings({ "resource" })
	public static void startDB() {

		System.out.println("Connecting to MongoDB...");

		try {

			MongoClientURI uri = new MongoClientURI("mongodb+srv://smarker:smarker@cluster0-0bzvo.mongodb.net/");

			MongoClient mongoClient = new MongoClient(uri);

			CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
					fromProviders(PojoCodecProvider.builder().automatic(true).build()));

			db = mongoClient.getDatabase("smarkerDB").withCodecRegistry(pojoCodecRegistry);

			BookMarkCollection = db.getCollection("Bookmarks", Bookmark.class);
			UserCollection = db.getCollection("Users", User.class);
			CommentCollection = db.getCollection("Comments", Comment.class);
			ErrorCollection = db.getCollection("Errors", ErrorSM.class);

			System.out.println("Connected!!!");

		} catch (Exception e) {
			System.out.println("Error:");
			System.out.println(e);
		}
	}

	

}
