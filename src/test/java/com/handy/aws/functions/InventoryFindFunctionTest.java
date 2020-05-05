package com.handy.aws.functions;

import static com.handy.aws.functions.InventoryFindFunction.BUCKET_NAME;
import static com.handy.aws.functions.InventoryFindFunction.KEY_NAME;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.adobe.testing.s3mock.junit4.S3MockRule;
import com.amazonaws.services.lambda.runtime.Context;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class InventoryFindFunctionTest {


	@ClassRule
	public static final S3MockRule s3mock = S3MockRule.builder().build();

	private static String input;

	@BeforeClass
	public static void createInput() throws IOException {
		// TODO: set up your sample input object here.
		input = "dummy";
	}

	final File uploadFile = new File("src/test/resources", KEY_NAME);
	
	@Before
	public void setupS3Client() {
		S3Client s3client = s3mock.createS3ClientV2();
		s3client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
		s3client.putObject(
				PutObjectRequest.builder().bucket(BUCKET_NAME).key(KEY_NAME).build(),
				uploadFile.toPath());
	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		// TODO: customize your context here if needed.
		ctx.setFunctionName("Your Function Name");

		return ctx;
	}

	@Test
	public void testInventoryFindFunction() {
		InventoryFindFunction handler = new InventoryFindFunction() {
			protected software.amazon.awssdk.services.s3.S3Client getS3Client() {
				return s3mock.createS3ClientV2();
			};
		};

		Context ctx = createContext();

		String output = handler.handleRequest(input, ctx);

		// TODO: validate output here if needed.
		assertThat(output, startsWith("Product[id=102"));
		assertThat(output, endsWith(",count=14]"));
	}
}
