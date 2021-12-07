package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

public class RequiredValidator implements ValueValidator {

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null != structureNode.getParent() && null == structureNode.getValue()) {
			return JsonValidationError.requiredValueAbsent(propertyName);
		}
		return null;
	}
}
