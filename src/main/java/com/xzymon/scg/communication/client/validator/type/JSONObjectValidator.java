package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;

import java.math.BigDecimal;

public class JSONObjectValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (Boolean) value);
			}
			if (value instanceof String) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (String) value);
			}
			if (value instanceof Integer) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (Integer) value);
			}
			if (value instanceof Long) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (Long) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotJSONObject(jsonPath, (JSONArray) value);
			}
		}
		return null;
	}
}
