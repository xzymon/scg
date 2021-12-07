package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class MaxValidator implements ValueValidator {
	private final int boundaryValue;

	public MaxValidator(int boundaryValue) {
		this.boundaryValue = boundaryValue;
	}

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof Integer) {
				Integer numericValue = (Integer) structureNode.getValue();
				if (numericValue > boundaryValue) {
					return JsonValidationError.intValueToHigh(propertyName, numericValue, boundaryValue);
				}
			}
		}
		return null;
	}
}
