package com.handy.aws.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class InventoryFindFunction implements RequestHandler<Object, String> {

	public static final String BUCKET_NAME = "handy-inventory-data-20200504";
	public static final String KEY_NAME = "handy-tool-catalog.json"; 

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        S3Client s3client = getS3Client();
		GetObjectRequest request = GetObjectRequest.builder()
				.bucket(BUCKET_NAME)
				.key(KEY_NAME)
				.build();

		String outputString;
		try (
				ResponseInputStream<?> objectData = s3client.getObject(request);
				InputStreamReader isr = new InputStreamReader(objectData);
				BufferedReader br = new BufferedReader(isr)) {
			
			outputString = br.readLine();
		} catch (IOException e) {
			context.getLogger().log("Error reading for s3testdata.txt " + e);
			outputString = null;
		}

        return outputString;
    }

	protected S3Client getS3Client() {
		return S3Client.builder()
				.region(Region.EU_WEST_2)
				.build();
	}

}
