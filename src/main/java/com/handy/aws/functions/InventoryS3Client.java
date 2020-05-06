package com.handy.aws.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

public class InventoryS3Client {
	public static final String BUCKET_NAME = "handy-inventory-data-20200504";
	public static final String KEY_NAME = "handy-tool-catalog.json"; 

	protected Product[] getAllProducts() {
		S3Client s3Client = buildClient();
        ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
        		.bucket(BUCKET_NAME)
        		.key(KEY_NAME)
        		.build());
        
        Product [] products = null;
        try (	InputStreamReader isr = new InputStreamReader(objectData);
        		BufferedReader br = new BufferedReader(isr)) {
        	
        	Gson gson = new Gson();
        	products = gson.fromJson(br, Product[ ].class);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

        return products;
	}

	protected ArrayList<Product> getAllProductsList() {
		return new ArrayList<Product>(Arrays.asList(getAllProducts()));
	}	
	
	protected boolean updateAllProducts(Product [] products) {
		Gson gson = new Gson(); 
        String jsonString = gson.toJson(products);
		
		S3Client s3Client = buildClient();
        PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
        		.bucket(BUCKET_NAME)
        		.key(KEY_NAME)
        		.build(),
        		RequestBody.fromString(jsonString));
        
        return putResponse.sdkHttpResponse().isSuccessful();
        
	}

	protected boolean updateAllProducts(List<Product> productList) {
		Product [] products = (Product[]) productList.toArray(new Product[productList.size()]);
		return updateAllProducts(products);
	}

	// protected for overriding in unit test (e.g. s3mock)
	protected S3Client buildClient() {
		Region region = Region.EU_WEST_2;
        return S3Client.builder().region(region).build();
	}

}
