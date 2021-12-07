package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class MinValidator implements ValueValidator {
	private final int boundaryValue;

	public MinValidator(int boundaryValue) {
		this.boundaryValue = boundaryValue;
	}

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof Integer) {
				Integer numericValue = (Integer) structureNode.getValue();
				if (numericValue < boundaryValue) {
					return JsonValidationError.intValueToLow(propertyName, numericValue, boundaryValue);
				}
			}
		}
		return null;
	}
}
