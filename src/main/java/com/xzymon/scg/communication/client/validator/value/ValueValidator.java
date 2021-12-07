package com.xzymon.scg.communication.client.validator.value;

import com.xzymon.scg.communication.client.JsonValidationWrapper;
import com.xzymon.scg.communication.client.validator.JsonValidationError;

public interface ValueValidator {
	JsonValidationError validate(String propertyName, JsonValidationWrapper.StructureNode value);
}
