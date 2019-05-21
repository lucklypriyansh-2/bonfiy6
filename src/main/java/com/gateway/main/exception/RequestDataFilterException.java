package com.gateway.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class RequestDataFilterException extends RuntimeException {

	public RequestDataFilterException(String string) {
	
		super(string);
	}
	
	public RequestDataFilterException(Exception e,String string) {
		
		super(string,e);
		
	}

}
