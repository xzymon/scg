package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class PatternValidator implements ValueValidator {
	private final String pattern;

	public PatternValidator(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof String) {
				String stringValue = (String) structureNode.getValue();
				if (!stringValue.matches(pattern)) {
					return JsonValidationError.patternMatchingUnsatisfied(propertyName, stringValue);
				}
			}
		}
		return null;
	}
}
