package com.handy.aws.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
@RunWith(MockitoJUnitRunner.class)
public class InventoryFindFunctionTest extends TestHelper {

	@Mock
	private InventoryS3Client client;
	
	private InventoryFindFunction handler;

	private QueryStringRequest request = new QueryStringRequest();

	@Before
	public void setupClient() throws IOException {
		handler = new InventoryFindFunction(client);
		when(client.getAllProducts()).thenReturn(products);
	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		// TODO: customize your context here if needed.
		ctx.setFunctionName("InventoryFindFunction");

		return ctx;
	}

	@Test
	public void testInventoryFindFunction102() {
		Context ctx = createContext();

		request.setQueryStringParameters(Collections.singletonMap("id", "102"));
		HttpProductResponse response = handler.handleRequest(request, ctx);

		Gson gson = new Gson();
		assertThat(response.getStatusCode(), is("200"));
		assertThat(response.getHeaders(), hasEntry("Content-Type", "application/json"));

		String expectedResponse = gson.toJson(product102);
		assertThat(response.getBody(), is(expectedResponse));
	}

	@Test
	public void testInventoryFindFunctionNotFound() {
		Context ctx = createContext();

		request.setQueryStringParameters(Collections.singletonMap("id", "99"));
		HttpProductResponse response = handler.handleRequest(request, ctx);

		assertThat(response.getStatusCode(), is("404"));
		assertThat(response.getHeaders(), hasEntry("Content-Type", "application/json"));

		assertThat(response.getBody(), nullValue());
	}

	@Test
	public void testInventoryFindFunctionAll() {
		Context ctx = createContext();

		request.setQueryStringParameters(Collections.singletonMap("id", "all"));
		HttpProductResponse response = handler.handleRequest(request, ctx);

		Gson gson = new Gson();
		assertThat(response.getStatusCode(), is("200"));
		assertThat(response.getHeaders(), hasEntry("Content-Type", "application/json"));

		String expectedResponse = gson.toJson(products);
		assertThat(response.getBody(), is(expectedResponse));
	}
}
