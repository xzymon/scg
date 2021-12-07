package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class IntegerValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotInteger(jsonPath, (Boolean) value);
			}
			if (value instanceof String) {
				return JsonValidationError.valueNotInteger(jsonPath, (String) value);
			}
			if (value instanceof Long) {
				return JsonValidationError.valueNotInteger(jsonPath, (Long) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotInteger(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotInteger(jsonPath, (JSONArray) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotInteger(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
