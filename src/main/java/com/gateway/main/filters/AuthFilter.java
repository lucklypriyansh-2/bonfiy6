package com.gateway.main.filters;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gateway.main.exception.AuthorisationException;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthFilter implements Filter {

	/**
	 * VALIDATE THE TOKEN BY CALLING IAM
	 */
	@Override
	public Object processData(String filterParam, Object data) {

		HttpServletRequest request = (HttpServletRequest) data;

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName  = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			if (headerName.equals("x-access-token")) {

				if (valid(headerValue)) {

				} else {
					
					throw new AuthorisationException("Not authorised token invalid");
				}

				

			}
		}

		return null;
	}

	/**
	 * check validation by calling IAM with token id
	 * @param headerValue
	 * @return
	 */
	private boolean valid(String headerValue) {
		//TODO All logic for validating from header 
		return true;
	}

}
