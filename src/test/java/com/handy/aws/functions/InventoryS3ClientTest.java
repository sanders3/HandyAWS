package com.handy.aws.functions;

import static com.handy.aws.functions.InventoryS3Client.BUCKET_NAME;
import static com.handy.aws.functions.InventoryS3Client.KEY_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.adobe.testing.s3mock.junit4.S3MockRule;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class InventoryS3ClientTest {

	@ClassRule
	public static final S3MockRule s3mock = S3MockRule.builder().build();

	private File uploadFile = new File("src/test/resources", KEY_NAME);

	private InventoryS3Client uut = new InventoryS3Client() {
		protected S3Client buildClient() {
			return s3mock.createS3ClientV2();
		};
	};

	private static Product product100 = new Product(100, "Hammer", "Stanley", "5oz Magnetic Tack Hammer", 20);
	private static Product product101 = new Product(101, "Hammer", "Wilton Bash", "24oz Ball Peen", 27);
	private static Product product102 = new Product(102, "Hammer", "DeWalt", "15oz MIG Weld", 14);
	private static Product product103 = new Product(103, "Hammer", "Crescent", "18 oz Pry Bar Hammer", 32);
	private static Product product104 = new Product(104,"Hammer", "DeWalt", "22 oz Mason Hammer", 7);

	private static Product newProduct105 = new Product(105,"Screw", "Uhu", "Crosshead", 99);

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
		ArrayList<Product> products = uut.getAllProductsList();
		assertThat(products,
				contains(product100, product101, product102, product103, product104));

		products.add(newProduct105);
		uut.updateAllProducts(products);

		assertThat(uut.getAllProductsList(),
				contains(product100, product101, product102, product103, product104, newProduct105));
	}
}
