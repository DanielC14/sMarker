package com.smarker.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.smarker.impl.ErrorSMManager;
import com.smarker.impl.UserManager;
import com.smarker.model.ErrorSM;
import com.smarker.model.User;

@Path("/errors")
public class ErrorSMResource {
	
	ErrorSMManager em = ErrorSMManager.getInstance();
	
	//GET an error by its code, returns JSON
	@Path("/{errorCode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorSM getErrorByCode(@PathParam("errorCode") String errorCode) {

		return em.getErrorByCode(errorCode);

	}
	
	//GET an error by its code, returns XML
	@Path("/{errorCode}/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public ErrorSM getErrorByCodeXML(@PathParam("errorCode") String errorCode) {

		return em.getErrorByCode(errorCode);

	}
	
	//GET all errors, returns JSON
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ErrorSM> getErrors() {

		return em.getErrors();

	}
	
	//GET all errors, returns XML
	@Path("/xml")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<ErrorSM> getErrorsXML() {

		return em.getErrors();

	}
	
	//POST an error
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public String createError(@FormParam("code") String code, @FormParam("message") String message, @FormParam("httpStatus") int httpStatus) {

		em.createError(code, message, httpStatus);
		
		return "success";

	}

}
