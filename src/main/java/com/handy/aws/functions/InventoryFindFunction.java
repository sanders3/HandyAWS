package com.handy.aws.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InventoryFindFunction implements RequestHandler<QueryStringRequest, HttpProductResponse> {
	
	private final InventoryS3Client client;

	// package level for testing
	InventoryFindFunction(InventoryS3Client client) {
		this.client = client;
	}

	public InventoryFindFunction() {
		this(new InventoryS3Client());
	}

	@Override
    public HttpProductResponse handleRequest(QueryStringRequest input, Context context) {
        context.getLogger().log("Input: " + input);

        String productIdString = input.getQueryStringParameters().get("id");
        if ("all".equalsIgnoreCase(productIdString)) {
        	return new HttpProductResponse(getAllProducts());
        }
        int productId = Integer.parseInt(productIdString);
        return new HttpProductResponse(getProductById(productId));
    }

	private Product[] getAllProducts() {
		return client.getAllProducts();
	}

	private Product getProductById(int id) {
		Product[] products = client.getAllProducts();
		for (Product product : products) {
			if (id == product.getId()) {
				return product;
			}
		}

		return null;
	}

}
