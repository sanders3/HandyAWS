package com.handy.aws.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
@RunWith(MockitoJUnitRunner.class)
public class InventoryDeleteFunctionTest extends TestHelper {

	private List<Product> productsList;

	@Mock private InventoryS3Client client;

	@Captor private ArgumentCaptor<List<Product>> captor;
	
    private HttpRequest request = new HttpRequest();

    private InventoryDeleteFunction handler;

	@Before
	public void setupClient() throws IOException {
		handler = new InventoryDeleteFunction(client);

		when(client.getAllProducts()).thenReturn(products);

		productsList = new ArrayList<>(Arrays.asList(products));
		when(client.getAllProductsList()).thenReturn(productsList);

		when(client.updateAllProducts(anyListOf(Product.class))).thenReturn(true);
	}

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("InventoryDeleteFunction");

        return ctx;
    }

    @Test
    public void testInventoryDeleteFunction() {
        Context ctx = createContext();

		request.setPathParameters(Collections.singletonMap("id", "103"));

		HttpProductResponse response = handler.handleRequest(request, ctx);

		assertThat(response.getBody(), nullValue());
        assertThat(response.getStatusCode(), is("200"));

        verify(client).updateAllProducts(captor.capture());
        List<Product> updatedProducts = captor.getValue();

        assertThat(updatedProducts, 
				contains(product100, product101, product102, product104));
    }

    @Test
    public void testInventoryDeleteFunctionNotFound() {
        Context ctx = createContext();

		request.setPathParameters(Collections.singletonMap("id", "99"));

		HttpProductResponse response = handler.handleRequest(request, ctx);

        assertThat(response.getBody(), nullValue());
        assertThat(response.getStatusCode(), is("404"));

        verify(client, never()).updateAllProducts(captor.capture());
    }

    @Test
    public void testInventoryDeleteFunctionError() {
        Context ctx = createContext();

		request.setPathParameters(Collections.singletonMap("id", "101"));
		when(client.updateAllProducts(anyListOf(Product.class))).thenReturn(false);

		HttpProductResponse response = handler.handleRequest(request, ctx);

        assertThat(response.getBody(), nullValue());
        assertThat(response.getStatusCode(), is("500"));

        verify(client).updateAllProducts(captor.capture());
        List<Product> updatedProducts = captor.getValue();

        assertThat(updatedProducts, 
				contains(product100, product102, product103, product104));
    }
}
