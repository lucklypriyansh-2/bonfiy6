package com.gateway.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class RequestTransformationException extends RuntimeException {

	public RequestTransformationException(String msg) {

		super(msg);
	}
}
