package com.gateway.main.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.gateway.main.config.ApiGatewayProperties;
import com.gateway.main.filters.FilterFactory;
import com.gateway.main.transformers.ContentRequestTransformer;
import com.gateway.main.transformers.HeadersRequestTransformer;
import com.gateway.main.transformers.URLRequestTransformer;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

@Component
public class HttpApi {

	@Autowired
	public ApiGatewayProperties apiGatewayProperties;

	@Autowired
	public HttpClient httpClient;

	@Autowired
	FilterFactory filterfactory;



	/**
	 * call async external url using NIO rx java
	 * 
	 * @param proxiedRequest
	 * @return
	 */
	Observable<String> callExternalUrlAsync(HttpUriRequest proxiedRequest) {

		return Observable.fromCallable(() -> callExternalUrl(proxiedRequest))
				.subscribeOn(Schedulers.io()).flatMap(re -> {

					if (re.hasBody())
						return Observable.just(re.getBody());
					else
						return Observable.error(
								new RuntimeException("Bad response status " + re.getStatusCode()));
				}, e -> Observable.error(e),
						(Func0<Observable<? extends String>>) (() -> Observable.empty()))
				.observeOn(Schedulers.computation());
	}

	/**
	 * 
	 * 
	 * @param proxiedRequest
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private ResponseEntity<String> callExternalUrl(HttpUriRequest proxiedRequest)
			throws ClientProtocolException, IOException {

		HttpResponse proxiedResponse = httpClient.execute(proxiedRequest);

		return new ResponseEntity<>(read(proxiedResponse.getEntity().getContent()),
				makeResponseHeaders(proxiedResponse),
				HttpStatus.valueOf(proxiedResponse.getStatusLine().getStatusCode()));

	}

	/**
	 * 
	 * Copying response header
	 * 
	 * @param response
	 * @return HttpHeaders
	 */
	private HttpHeaders makeResponseHeaders(HttpResponse response) {
		HttpHeaders result = new HttpHeaders();
		Header      h      = response.getFirstHeader("Content-Type");
		for (Header header : response.getAllHeaders()) {
			result.set(header.getName(), header.getValue());
		}

		return result;
	}

	/**
	 * 
	 * @param request
	 * @return HttpUriRequest
	 */
	public HttpUriRequest createHttpUriRequest(HttpServletRequest request) {

		URLRequestTransformer     urlRequestTransformer     = new URLRequestTransformer(
				apiGatewayProperties, filterfactory);
		ContentRequestTransformer contentRequestTransformer = new ContentRequestTransformer(
				apiGatewayProperties, filterfactory);
		HeadersRequestTransformer headersRequestTransformer = new HeadersRequestTransformer(
				apiGatewayProperties, filterfactory);
		headersRequestTransformer.setPredecessor(contentRequestTransformer);
		contentRequestTransformer.setPredecessor(urlRequestTransformer);

		return headersRequestTransformer.transform(request).build();

	}

	private String read(InputStream input) {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}