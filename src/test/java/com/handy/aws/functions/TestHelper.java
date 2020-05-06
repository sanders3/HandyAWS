package com.handy.aws.functions;

public class TestHelper {

	public static Product product100 = new Product(100, "Hammer", "Stanley", "5oz Magnetic Tack Hammer", 20);
	public static Product product101 = new Product(101, "Hammer", "Wilton Bash", "24oz Ball Peen", 27);
	public static Product product102 = new Product(102, "Hammer", "DeWalt", "15oz MIG Weld", 14);
	public static Product product103 = new Product(103, "Hammer", "Crescent", "18 oz Pry Bar Hammer", 32);
	public static Product product104 = new Product(104,"Hammer", "DeWalt", "22 oz Mason Hammer", 7);

	public static Product[] products = { product100, product101, product102, product103, product104 };

	public static Product newProduct105 = new Product(105,"Screw", "Uhu", "Crosshead", 99);

}
