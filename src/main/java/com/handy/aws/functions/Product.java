package com.handy.aws.functions;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Product {

	private int id;
	private String toolType;
	private String brand;
	private String name;
	private int count;

	public Product() {
		this.id = -1;
	}
	
	public Product(int id, String toolType, String brand, String name, int count) {
		this.id = id;
		this.toolType = toolType;
		this.brand = brand;
		this.name = name;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToolType() {
		return toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.append(this.toolType)
				.append(this.brand)
				.append(this.name)
				.append(this.count)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Product other = (Product) obj;
		return new EqualsBuilder()
				.append(this.id, other.id)
				.append(this.toolType, other.toolType)
				.append(this.brand, other.brand)
				.append(this.name, other.name)
				.append(this.count, other.count)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("toolType", toolType)
				.append("brand", brand)
				.append("name", name)
				.append("count", count)
				.build();
	}
}
