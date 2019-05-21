package com.gateway.main.config;

import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author p0n004h
 *
 */
@Configuration
public class BeanConfig {

	@Bean
	public HttpClient getHttpClient(HttpRequestRetryHandler requestRetryHandler) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

		HttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(retryHandler()).build();

		return httpClient;
	}

	public static HttpRequestRetryHandler retryHandler() {
		return (exception, executionCount, context) -> {

			System.out.println("try request: " + executionCount);

			if (executionCount >= 5) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				// Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) {
				// Unknown host
				return false;
			}
			if (exception instanceof SSLException) {
				// SSL handshake exception
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest       request       = clientContext.getRequest();
			boolean           idempotent    = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		};
	}
}
