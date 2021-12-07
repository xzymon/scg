package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class StringValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotString(jsonPath, (Boolean) value);
			}
			if (value instanceof Integer) {
				return JsonValidationError.valueNotString(jsonPath, (Integer) value);
			}
			if (value instanceof Long) {
				return JsonValidationError.valueNotString(jsonPath, (Long) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotString(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotString(jsonPath, (JSONArray) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotString(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
