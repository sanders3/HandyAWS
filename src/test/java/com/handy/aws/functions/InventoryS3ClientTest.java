package com.handy.aws.functions;

import static com.handy.aws.functions.InventoryS3Client.BUCKET_NAME;
import static com.handy.aws.functions.InventoryS3Client.KEY_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.adobe.testing.s3mock.junit4.S3MockRule;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class InventoryS3ClientTest extends TestHelper {

	@ClassRule
	public static final S3MockRule s3mock = S3MockRule.builder().build();

	private File uploadFile = new File("src/test/resources", KEY_NAME);

	private InventoryS3Client uut = new InventoryS3Client() {
		protected S3Client buildClient() {
			return s3mock.createS3ClientV2();
		};
	};

	@Before
	public void setupS3Client() {
		S3Client s3client = s3mock.createS3ClientV2();
		s3client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
		s3client.putObject(
				PutObjectRequest.builder().bucket(BUCKET_NAME).key(KEY_NAME).build(),
				uploadFile.toPath());
	}

	@Test
	public void testGetAllProducts() throws IOException {
		assertThat(uut.getAllProducts(),
				arrayContaining(product100, product101, product102, product103, product104));
	}

	@Test
	public void testGetAllProductsList() throws IOException {
		assertThat(uut.getAllProductsList(),
				contains(product100, product101, product102, product103, product104));
	}


	@Test
	public void testUpdateAllProductsAddItem() throws IOException {
		List<Product> products = uut.getAllProductsList();
		assertThat(products,
				contains(product100, product101, product102, product103, product104));

		products.add(newProduct105);
		uut.updateAllProducts(products);

		assertThat(uut.getAllProductsList(),
				contains(product100, product101, product102, product103, product104, newProduct105));
	}
}
