package com.handy.aws.functions;

import static org.hamcrest.CoreMatchers.is;
import static pl.pojo.tester.api.assertion.Method.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import org.junit.Test;

import com.google.gson.Gson;

public class ProductTest {

	@Test
	public void pojoWellImplemented() {
	    assertPojoMethodsFor(Product.class)
	    	.testing(EQUALS, HASH_CODE, GETTER, SETTER, CONSTRUCTOR)
	    		.areWellImplemented();
	}

	private static final String JSON = "{" 
				+ "\"id\":100," 
				+ "\"toolType\":\"Hammer\"," 
				+ "\"brand\":\"Stanley\"," 
				+ "\"name\":\"5oz Magnetic Tack Hammer\"," 
				+ "\"count\":20" 
				+ "}";

	@Test
	public void unmarshal() {
		Product actual = new Gson().fromJson(JSON, Product.class);
		Product expected = new Product(100, "Hammer", "Stanley", "5oz Magnetic Tack Hammer", 20);
		assertThat(actual, is(expected));
	}

	@Test
	public void marshal() {
		Product product = new Product(100, "Hammer", "Stanley", "5oz Magnetic Tack Hammer", 20);
		String actual = new Gson().toJson(product);
		assertThat(actual, is(JSON));
	}
}
