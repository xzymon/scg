package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class BooleanValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof String) {
				return JsonValidationError.valueNotBoolean(jsonPath, (String) value);
			}
			if (value instanceof Integer) {
				return JsonValidationError.valueNotBoolean(jsonPath, (Integer) value);
			}
			if (value instanceof Long) {
				return JsonValidationError.valueNotBoolean(jsonPath, (Long) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotBoolean(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotBoolean(jsonPath, (JSONArray) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotBoolean(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
