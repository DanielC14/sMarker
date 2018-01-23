package com.smarker.impl;

import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.Block;
import com.smarker.init.MongoDB;
import com.smarker.model.User;
import com.smarker.model.ErrorSM;

public class ErrorSMManager {
	
	static ErrorSMManager em = null;
	
	public static ErrorSMManager getInstance() {
		if (em == null) {
			em = new ErrorSMManager();
		}
		return em;
	}
	
	
	//Gets a list of all the errors
	public List<ErrorSM> getErrors(){
		
		List<ErrorSM> errors = new ArrayList<ErrorSM>();

		Block<ErrorSM> printErrors= new Block<ErrorSM>() {
			public void apply(final ErrorSM error) {
				errors.add(error);
			}
		};

		MongoDB.ErrorCollection.find().forEach(printErrors);
		
		return errors;
	}
	
	//Gets an error by its code
	public ErrorSM getErrorByCode(String code) {
		
		MongoDB.ErrorCollection.find(eq("code", code)).first();
		
		return MongoDB.ErrorCollection.find(eq("code", code)).first();
		
	}
	
	//creates an error
	public void createError(String code, String message, int httpStatus)
	{
		if(MongoDB.ErrorCollection.find(eq("code", code)).first() == null)
		{
			ErrorSM e = new ErrorSM(code, message, httpStatus);
			MongoDB.ErrorCollection.insertOne(e);
		}
	}

}
