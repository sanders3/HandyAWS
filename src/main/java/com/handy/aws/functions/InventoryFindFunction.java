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

public class InventoryFindFunction implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        context.getLogger().log("Input: " + input);

        Region region = Region.EU_WEST_2;
		S3Client s3client = S3Client.builder().region(region).build();
		GetObjectRequest request = GetObjectRequest.builder()
				.bucket("handy-inventory-data-20200504")
				.key("s3testdata.txt")
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

}
