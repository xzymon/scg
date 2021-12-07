package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;

public interface TypeValidator {
	JsonValidationError validate(String jsonPath, Object value);
}
