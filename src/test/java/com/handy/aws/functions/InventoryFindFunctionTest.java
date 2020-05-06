package com.handy.aws.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
public class InventoryFindFunctionTest {

	private static Product product100 = new Product(100, "Hammer", "Stanley", "5oz Magnetic Tack Hammer", 20);
	private static Product product101 = new Product(101, "Hammer", "Wilton Bash", "24oz Ball Peen", 27);
	private static Product product102 = new Product(102, "Hammer", "DeWalt", "15oz MIG Weld", 14);
	private static Product product103 = new Product(103, "Hammer", "Crescent", "18 oz Pry Bar Hammer", 32);
	private static Product product104 = new Product(104,"Hammer", "DeWalt", "22 oz Mason Hammer", 7);

	private static Product[] products = { product100, product101, product102, product103, product104 };

	@Mock
	private InventoryS3Client client;
	
	private InventoryFindFunction handler;

	private QueryStringRequest input = new QueryStringRequest();

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

		input.setQueryStringParameters(Collections.singletonMap("id", "102"));
		HttpProductResponse response = handler.handleRequest(input, ctx);

		Gson gson = new Gson();
		assertThat(response.getStatusCode(), is("200"));
		assertThat(response.getHeaders(), hasEntry("Content-Type", "application/json"));

		String expectedResponse = gson.toJson(product102);
		assertThat(response.getBody(), is(expectedResponse));
	}

	@Test
	public void testInventoryFindFunctionAll() {
		Context ctx = createContext();

		input.setQueryStringParameters(Collections.singletonMap("id", "all"));
		HttpProductResponse response = handler.handleRequest(input, ctx);

		Gson gson = new Gson();
		assertThat(response.getStatusCode(), is("200"));
		assertThat(response.getHeaders(), hasEntry("Content-Type", "application/json"));

		String expectedResponse = gson.toJson(products);
		assertThat(response.getBody(), is(expectedResponse));
	}
}
