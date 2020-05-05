package com.handy.aws.functions;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QueryStringRequest {

	// TODO: this will not cope with repeated query parameters
	private Map<String, String> parameters;

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.parameters)
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

		QueryStringRequest other = (QueryStringRequest) obj;
		return new EqualsBuilder()
				.append(this.parameters, other.parameters)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("parameters", parameters)
				.build();
	}}
