package com.gateway.main.transformers;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;

import com.gateway.main.config.ApiGatewayProperties;
import com.gateway.main.filters.FilterFactory;

public class HeadersRequestTransformer extends ProxyRequestTransformer {

	private ApiGatewayProperties apiGatewayProperties;

	public HeadersRequestTransformer(ApiGatewayProperties apiGatewayProperties, FilterFactory f) {
		this.apiGatewayProperties = apiGatewayProperties;
		this.filterfactory        = f;
	}

	@Override
	public RequestBuilder transform(HttpServletRequest request) {
		RequestBuilder requestBuilder = null;
		requestBuilder = predecessor.transform(request);

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName  = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			if (headerName.equals("x-access-token")) {
				requestBuilder.addHeader(headerName, headerValue);
			}
		}

		return requestBuilder;


	}
}
