package com.handy.aws.functions;

import java.util.HashMap;
import java.util.Map;

public class HttpProductResponse {

	private String body;

	private String statusCode;

	private Map<String, String> headers = new HashMap<>();

	public HttpProductResponse() {
		this.headers.put("Content-Type", "application/json");
	}

	public HttpProductResponse(Product produce) {
		this();
		body = produce.toString();
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	
}
