package com.handy.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InventoryDeleteFunction implements RequestHandler<HttpRequest, HttpProductResponse> {
	private final InventoryS3Client client;

	// package level for testing
	InventoryDeleteFunction(InventoryS3Client client) {
		this.client = client;
	}

	public InventoryDeleteFunction() {
		this(new InventoryS3Client());
	}

	@Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Request: " + request);

        String productIdString = request.getPathParameters().get("id");
        int productId = Integer.parseInt(productIdString);

        List<Product> products = client.getAllProductsList();
        boolean removed = products.removeIf(p -> productId == p.getId());
        if (removed) {
			if (client.updateAllProducts(products)) {
				return new HttpProductResponse();
			} else {
		        HttpProductResponse errorResponse = new HttpProductResponse();
		        errorResponse.setStatusCode("500");
		        return errorResponse;
			}
		} else {
			HttpProductResponse notFoundResponse = new HttpProductResponse();
			notFoundResponse.setStatusCode("404");
			return notFoundResponse;
		}
    }

}
