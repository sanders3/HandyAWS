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
    public HttpProductResponse handleRequest(QueryStringRequest request, Context context) {
        context.getLogger().log("Request: " + request);

        String productIdString = request.getQueryStringParameters().get("id");
        if ("all".equalsIgnoreCase(productIdString)) {
        	return new HttpProductResponse(getAllProducts());
        }
        int productId = Integer.parseInt(productIdString);
        Product product = getProductById(productId);
        if (product != null) {
        	return new HttpProductResponse(product);
        } else {
        	HttpProductResponse notFound = new HttpProductResponse();
        	notFound.setStatusCode("404");
			return notFound;
        }
    }

	private Product[] getAllProducts() {
		return client.getAllProducts();
	}

	private Product getProductById(int productId) {
		Product[] products = client.getAllProducts();
		for (Product product : products) {
			if (productId == product.getId()) {
				return product;
			}
		}

		return null;
	}

}
