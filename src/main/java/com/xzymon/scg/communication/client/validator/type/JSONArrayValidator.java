package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONObject;

import java.math.BigDecimal;

public class JSONArrayValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (Boolean) value);
			}
			if (value instanceof String) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (String) value);
			}
			if (value instanceof Integer) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (Integer) value);
			}
			if (value instanceof Long) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (Long) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotJSONArray(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
