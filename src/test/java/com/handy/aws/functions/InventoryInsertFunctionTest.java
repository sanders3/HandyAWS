package com.handy.aws.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class InventoryInsertFunctionTest extends TestHelper {

	private List<Product> productsList;

	@Mock private InventoryS3Client client;

	@Captor private ArgumentCaptor<List<Product>> captor;
	
    private HttpRequest request = new HttpRequest();

    private InventoryInsertFunction handler;

	@Before
	public void setupClient() throws IOException {
		handler = new InventoryInsertFunction(client);

		when(client.getAllProducts()).thenReturn(products);

		productsList = new ArrayList<>(Arrays.asList(products));
		when(client.getAllProductsList()).thenReturn(productsList);

		when(client.updateAllProducts(anyListOf(Product.class))).thenReturn(true);
	}

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("InventoryFindFunction");

        return ctx;
    }

    @Test
    public void testInventoryInsertFunction() {
        Context ctx = createContext();

        Gson gson = new Gson();
      
        String newProductString = gson.toJson(newProduct105);
		request.setBody(newProductString);
        HttpProductResponse response = handler.handleRequest(request, ctx);
        assertThat(response.getBody(), nullValue());
        assertThat(response.getStatusCode(), is("200"));

        verify(client).updateAllProducts(captor.capture());
        List<Product> updatedProducts = captor.getValue();

        assertThat(updatedProducts, 
				contains(product100, product101, product102, product103, product104, newProduct105));
    }
}
