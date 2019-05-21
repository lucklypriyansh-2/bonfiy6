package com.gateway.main.transformers;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.gateway.main.config.ApiGatewayProperties;
import com.gateway.main.exception.RequestTransformationException;
import com.gateway.main.filters.FilterFactory;

public class ContentRequestTransformer extends ProxyRequestTransformer {

	private ApiGatewayProperties apiGatewayProperties;

	public ContentRequestTransformer(ApiGatewayProperties apiGatewayProperties, FilterFactory f) {
		this.apiGatewayProperties = apiGatewayProperties;
		this.filterfactory        = f;
	}

	@Override
	public RequestBuilder transform(HttpServletRequest request)
			  {
		RequestBuilder requestBuilder = predecessor.transform(request);
		try {
			String requestContent;

			requestContent = request.getReader().lines().collect(Collectors.joining(""));

			if (!requestContent.isEmpty()) {
				StringEntity entity = new StringEntity(requestContent,
						ContentType.APPLICATION_JSON);
				requestBuilder.setEntity(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RequestTransformationException(e.getMessage());

		}
		return requestBuilder;
	}
}
