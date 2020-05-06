package com.handy.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class InventoryInsertFunction implements RequestHandler<HttpRequest, HttpProductResponse> {

	private final InventoryS3Client client;

	// package level for testing
	InventoryInsertFunction(InventoryS3Client client) {
		this.client = client;
	}

	public InventoryInsertFunction() {
		this(new InventoryS3Client());
	}

	@Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Request: " + request);

        String newProductString = request.getBody();
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(newProductString, Product.class);

        List<Product> products = client.getAllProductsList();
        products.add(newProduct);
        
        if (client.updateAllProducts(products)) {
        	return new HttpProductResponse();
        }

        HttpProductResponse errorResponse = new HttpProductResponse();
        errorResponse.setStatusCode("500");
        return errorResponse;
    }

}
