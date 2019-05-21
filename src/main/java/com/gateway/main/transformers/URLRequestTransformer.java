package com.gateway.main.transformers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gateway.main.config.ApiGatewayProperties;
import com.gateway.main.config.ApiGatewayProperties.Endpoint;
import com.gateway.main.exception.RequestTransformationException;
import com.gateway.main.filters.FilterFactory;

public class URLRequestTransformer extends ProxyRequestTransformer {

	private ApiGatewayProperties apiGatewayProperties;

	public URLRequestTransformer(ApiGatewayProperties apiGatewayProperties, FilterFactory f) {
		this.apiGatewayProperties = apiGatewayProperties;
		this.filterfactory        = f;
	}

	@Override
	public RequestBuilder transform(HttpServletRequest request) {
		try {
			String requestURI = request.getRequestURI();

			URI uri;
			if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {

				uri = new URI(getServiceUrl(requestURI, request) + "?" + request.getQueryString());

			} else {
				uri = new URI(getServiceUrl(requestURI, request));
			}

			RequestBuilder rb = RequestBuilder.create(request.getMethod());
			rb.setUri(uri);
			return rb;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RequestTransformationException(e.getMessage());
			
		}
	}

	/**
	 * Load Distribution using multiple urls and randomly selecting one
	 * 
	 * @param requestURI
	 * @param httpServletRequest
	 * @return
	 * @throws NoSuchRequestHandlingMethodException
	 */
	private String getServiceUrl(String requestURI, HttpServletRequest httpServletRequest) {

		Optional<Endpoint> endpoint = apiGatewayProperties.getEndpoints().stream()
				.filter(e -> requestURI.matches(e.getPath())
						&& e.getMethod() == RequestMethod.valueOf(httpServletRequest.getMethod()))
				.findFirst();

		if (endpoint.isPresent()) {

			Random rand = new Random();
			return endpoint.get().getLocation()
					.get(rand.nextInt(endpoint.get().getLocation().size())) + requestURI;

		} else {
			throw new NoSuchRequestHandlingMethodException("NO HANDLING METHOD FOUND");
		}

	}
}
