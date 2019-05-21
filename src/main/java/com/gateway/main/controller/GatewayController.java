package com.gateway.main.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.main.config.ApiGatewayProperties;
import com.gateway.main.filters.FilterFactory;
import com.gateway.main.transformers.NoSuchRequestHandlingMethodException;

import rx.Observable;

/**
 * 
 * @author p0n004h
 *
 */
@RestController
public class GatewayController {
	
	
	@Autowired
	ApiGatewayProperties apigatewayproperties;

	@Autowired
	FilterFactory filterFactory;

	@Autowired
	private HttpApi httpapi;

	@RequestMapping(value = "/**", method = { GET, POST, DELETE })
	@ResponseBody
	public Observable<String> proxyRequest(HttpServletRequest request) {
		try {
			applyRequestFilter(request);
			HttpUriRequest proxiedRequest = httpapi.createHttpUriRequest(request);
			return httpapi.callExternalUrlAsync(proxiedRequest);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Apply request filter
	 * 
	 * @param httpServletRequest
	 */
	private void applyRequestFilter(HttpServletRequest httpServletRequest) {

		String                        requestURI = httpServletRequest.getRequestURI();
		ApiGatewayProperties.Endpoint endpoint   = apigatewayproperties.getEndpoints().stream()
				.filter(e -> requestURI.matches(e.getPath())
						&& e.getMethod() == RequestMethod.valueOf(httpServletRequest.getMethod()))
				.findFirst().orElseThrow(() -> new NoSuchRequestHandlingMethodException("No Mapping Found"));

		for (ApiGatewayProperties.Filter filter : endpoint.getFilters()) {
			if (filter.getFilterType().equalsIgnoreCase("REQUEST")) {
				filterFactory.getInstance(filter.getAssociatedFilter())
						.processData(filter.getFiltervalue(), httpServletRequest);
			}
		}

	}

}
