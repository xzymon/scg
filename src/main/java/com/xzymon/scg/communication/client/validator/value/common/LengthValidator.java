package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class LengthValidator implements ValueValidator {
	private final int length;

	public LengthValidator(int length) {
		this.length = length;
	}

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof String) {
				String stringValue = (String) structureNode.getValue();
				if (stringValue.length() != length) {
					return JsonValidationError.lengthIncorrect(propertyName, stringValue, length);
				}
			}
		}
		return null;
	}
}