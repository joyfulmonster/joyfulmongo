package com.joyfulmongo.controller;

public class JFCConstants
{
	public enum Props
	{
		// result
		result,
		results,
		
		// comman
		data,
		classname,
		session_token,
		iid,
		uuid,
		v,
		code, 
		error,
		
		// login
		username,
		user_password,
		
		// mult
		commands, 
		params, 
		op,
		
		// find
		limit,
		skip,
		fields, 
		order, 
		include, 
		redirectClassNameForKey, // this is special condition for relation query  
	}
}

