package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class DictionaryValidator implements ValueValidator {
	private final String[] allowedStringValues;

	public DictionaryValidator(String[] allowedValues) {
		this.allowedStringValues = allowedValues;
	}

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			boolean match = false;
			if (structureNode.getValue() instanceof String) {
				String stringValue = (String) structureNode.getValue();
				for (String allowedValue : allowedStringValues) {
					if (allowedValue.equals(stringValue)) {
						match = true;
						break;
					}
				}
				if (!match) {
					return JsonValidationError.dictionaryValueIncorrect(propertyName, stringValue);
				}
			}
		}
		return null;
	}
}
